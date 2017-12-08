package com.unuldur.uminc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("etudiant", etudiant);
        startActivity(intent);
        finish();
    }
}
