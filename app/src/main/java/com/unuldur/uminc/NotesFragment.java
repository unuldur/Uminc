package com.unuldur.uminc;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.unuldur.uminc.model.Etudiant;
import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.Note;
import com.unuldur.uminc.model.UE;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Unuldur on 24/01/2018.
 */

public class NotesFragment extends Fragment {
    private static final String ETUDIANT_KEY = "etudiant";
    private IEtudiant etudiant;
    private ExpandableListView lv;
    private SyncroniseNotesIn sync;
    public static NotesFragment newInstance(IEtudiant etudiant) {
        NotesFragment pf = new NotesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ETUDIANT_KEY, etudiant);
        pf.setArguments(bundle);
        return pf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        etudiant = (IEtudiant) getArguments().getSerializable(ETUDIANT_KEY);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Notes");
        lv = getActivity().findViewById(R.id.lvNotes);
        List<String> head = new ArrayList<>();
        HashMap<String, List<String>> hash = new HashMap<>();
        for(Note note : etudiant.getNotes()){
            String id = note.getUE().getId()+ " - " + note.getUE().getName();
            if(!hash.containsKey(id)){
                hash.put(id, new ArrayList<String>());
                head.add(id);
            }
            hash.get(id).add(note.getName() + " " + note.getNote());
        }
        ExpandableListAdapter adapter  = new NoteExandableListAdapater(getContext(), head, hash);
        lv.setAdapter(adapter);
        for (int i = 0; i < lv.getChildCount(); i++) {
            lv.expandGroup(i);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sync_item) {
            if(sync == null){
                sync = new SyncroniseNotesIn(etudiant, getContext());
                sync.execute();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SyncroniseNotesIn extends SyncroniseNotes {

        public SyncroniseNotesIn(IEtudiant etudiant, Context context) {
            super(etudiant, context);
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getContext(), "Synchronisation en cours...", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            sync = null;

            if(notes != null){
                etudiant.setNotes(notes);
                String filename = getString(R.string.etudiant_saver);
                FileOutputStream outputStream;
                try {
                    outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    oos.writeObject(etudiant);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<String> ls = new ArrayList<>();
                for (Note note: etudiant.getNotes()){
                    ls.add(note.toString());
                }
                Toast.makeText(getContext(), "Synchronisation terminé", Toast.LENGTH_SHORT).show();
                List<String> head = new ArrayList<>();
                HashMap<String, List<String>> hash = new HashMap<>();
                for(Note note : etudiant.getNotes()){
                    String id = note.getUE().getId()+ " - " + note.getUE().getName();
                    if(!hash.containsKey(id)){
                        hash.put(id, new ArrayList<String>());
                        head.add(id);
                    }
                    hash.get(id).add(note.getName() + " " + note.getUE());
                }
                ExpandableListAdapter adapter  = new NoteExandableListAdapater(getContext(), head, hash);
                lv.setAdapter(adapter);
            }else{
                Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            sync = null;
        }
    }
}
