package com.unuldur.uminc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.unuldur.uminc.calendarRecuperator.FullCalendarRecuperator;
import com.unuldur.uminc.calendarRecuperator.ICalendarRecuperator;
import com.unuldur.uminc.model.DbufrConnection;
import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IDbufrConnection;
import com.unuldur.uminc.model.IEtudiant;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Unuldur on 07/12/2017.
 */

public class UeChooserActivity extends AppCompatActivity implements View.OnClickListener {
    private IEtudiant etudiant;
    private GetCalendarTask mCalendarTask = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        etudiant = (IEtudiant)intent.getSerializableExtra("etudiant");
        setContentView(R.layout.ue_chooser);
        ListView ll = findViewById(R.id.ueChooserLv);
        ListAdapter adapter = new EtudiantUeChooserAdapter(this,android.R.layout.list_content, etudiant);
        ll.setAdapter(adapter);

        Button b = findViewById(R.id.ueChooserButton);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(mCalendarTask != null) return;
        mCalendarTask = new GetCalendarTask(new FullCalendarRecuperator(getBaseContext()), etudiant);
        mCalendarTask.execute((Void) null);
    }

    public class GetCalendarTask extends AsyncTask<Void, Void, ICalendar> {

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
            if (success != null) {
                etudiant.addCalendar(success);
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
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("etudiant", etudiant);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getBaseContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mCalendarTask = null;
        }
    }
}
