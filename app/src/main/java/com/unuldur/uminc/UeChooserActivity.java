package com.unuldur.uminc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.unuldur.uminc.model.IEtudiant;

/**
 * Created by Unuldur on 07/12/2017.
 */

public class UeChooserActivity extends AppCompatActivity {
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
    }
}
