package com.unuldur.uminc;

import android.content.Context;
import android.os.AsyncTask;

import com.unuldur.uminc.model.DbufrConnection;
import com.unuldur.uminc.model.Etudiant;
import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.Note;

import java.util.List;

/**
 * Created by Unuldur on 24/01/2018.
 */

public class SyncroniseNotes extends AsyncTask<Void, Void, List<Note>> {
    private IEtudiant etudiant;
    private Context context;
    public SyncroniseNotes(IEtudiant etudiant, Context context) {
        this.etudiant = etudiant;
        this.context = context;
    }

    @Override
    protected List<Note> doInBackground(Void... voids) {
        DbufrConnection dbufr = new DbufrConnection(context);
        IEtudiant e = dbufr.connect(etudiant.getNUmEtu(), etudiant.getPassword());
        if(e == null) return null;
        return e.getNotes();
    }


}
