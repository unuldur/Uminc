package com.unuldur.uminc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.Note;
import com.unuldur.uminc.model.UE;

import java.util.ArrayList;
import java.util.List;


public class AccueilFragment extends Fragment{

    private static final String ETUDIANT_KEY = "etudiant";
    private IEtudiant etudiant;

    public AccueilFragment() {
        // Required empty public constructor
    }

    public static AccueilFragment newInstance(IEtudiant etudiant) {

        AccueilFragment pf = new AccueilFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ETUDIANT_KEY, etudiant);
        pf.setArguments(bundle);
        return pf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        etudiant = (IEtudiant) getArguments().getSerializable(ETUDIANT_KEY);
            // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accueil, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Accueil");
        TextView tv = getActivity().findViewById(R.id.accueil_hello_message);
        tv.setText("Bonjour étudiant n°" + etudiant.getNUmEtu());

        ListView lv = getActivity().findViewById(R.id.accueil_list);
        List<String> ls = new ArrayList<>();
        for (UE ue: etudiant.getActualUEs()){
            ls.add(ue.toString());
        }
        ListAdapter adapter = new ArrayAdapter<>(getActivity().getBaseContext(),android.R.layout.simple_list_item_1, ls);
        lv.setAdapter(adapter);
    }

}
