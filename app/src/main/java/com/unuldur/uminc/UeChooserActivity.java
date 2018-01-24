package com.unuldur.uminc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.unuldur.uminc.calendarRecuperator.FullCalendarRecuperator;
import com.unuldur.uminc.calendarRecuperator.ICalendarRecuperator;
import com.unuldur.uminc.model.DbufrConnection;
import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IDbufrConnection;
import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.UE;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unuldur on 07/12/2017.
 */

public class UeChooserActivity extends AppCompatActivity implements View.OnClickListener {
    private IEtudiant etudiant;
    private GetCalendarTask mCalendarTask = null;
    private SyncronyseUes syncronyseUes = null;
    private LinearLayout mlinearLayout;
    private ProgressBar mprogressView;
    private ListView lv;
    private List<UE> chang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        etudiant = (IEtudiant)intent.getSerializableExtra("etudiant");
        setContentView(R.layout.ue_chooser);
        lv = findViewById(R.id.ueChooserLv);
        ListAdapter adapter = new EtudiantUeChooserAdapter(this,android.R.layout.list_content, etudiant);
        lv.setAdapter(adapter);

        Button b = findViewById(R.id.ueChooserButton);
        b.setOnClickListener(this);

        mlinearLayout =  findViewById(R.id.linearChooser);
        mprogressView = findViewById(R.id.progressBar_chooser);

        chang = new ArrayList<>(etudiant.getActualUEs());
    }

    @Override
    public void onClick(View view) {
        if(mCalendarTask != null || syncronyseUes != null) return;
        if(chang.equals(etudiant.getActualUEs())) {
            goToHome();
            return;}
        mCalendarTask = new GetCalendarTask(new FullCalendarRecuperator(getBaseContext()), etudiant);
        showProgress(true);
        mCalendarTask.execute((Void) null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mlinearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        mlinearLayout.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mlinearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mprogressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mprogressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mprogressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sync_item) {
            if (syncronyseUes == null) {
                syncronyseUes = new SyncronyseUes(etudiant.getNUmEtu(), etudiant.getPassword());
                showProgress(true);
                syncronyseUes.execute((Void) null);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToHome(){
        String filename = getString(R.string.etudiant_saver);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(etudiant);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AlarmManagerEvent.getInstance(this).createNotifications(etudiant.getCalendar().getAllEvents(), 15 * 60 * 1000);
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("etudiant", etudiant);
        startActivity(intent);
        finish();
    }

    private class GetCalendarTask extends AsyncTask<Void, Void, ICalendar> {

        private final ICalendarRecuperator connection;
        private final IEtudiant etudiant;
        GetCalendarTask(ICalendarRecuperator cr, IEtudiant etudiant) {
            connection = cr;
            this.etudiant = etudiant;
        }

        @Override
        protected ICalendar doInBackground(Void... params) {
            return connection.getCalendar(etudiant);
        }

        @Override
        protected void onPostExecute(final ICalendar success) {
            mCalendarTask = null;
            showProgress(false);
            if (success != null) {
                etudiant.addCalendar(success);
                goToHome();
            }else{
                Toast.makeText(getBaseContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onCancelled() {
            mCalendarTask = null;
            showProgress(false);
        }
    }

    private class SyncronyseUes  extends AsyncTask<Void, Void, IEtudiant> {
        private final String mNumEtu;
        private final String mPassword;
        private final IDbufrConnection connection;
        SyncronyseUes(String numEtu, String password) {
            mNumEtu = numEtu;
            mPassword = password;
            connection = new DbufrConnection(getBaseContext());
        }

        @Override
        protected IEtudiant doInBackground(Void... params) {
            return connection.connect(mNumEtu, mPassword);
        }

        @Override
        protected void onPostExecute(final IEtudiant success) {
            syncronyseUes = null;
            showProgress(false);

            if (success != null) {
                for (UE ue: success.getUEs()){
                    if(!etudiant.getUEs().contains(ue)){
                        etudiant.getUEs().add(ue);
                    }
                }
                ListAdapter adapter = new EtudiantUeChooserAdapter(getBaseContext(),android.R.layout.list_content, etudiant);
                lv.setAdapter(adapter);
            } else {
                Toast.makeText(getBaseContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            syncronyseUes = null;
            showProgress(false);
        }
    }
}
