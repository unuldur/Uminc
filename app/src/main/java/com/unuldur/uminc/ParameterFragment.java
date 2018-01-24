package com.unuldur.uminc;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.unuldur.uminc.model.Etudiant;
import com.unuldur.uminc.model.IEtudiant;

/**
 * Created by julie on 08/12/2017.
 */

public class ParameterFragment extends Fragment implements View.OnClickListener{

    private static final String ETUDIANT_KEY = "etudiant";
    private IEtudiant etudiant;
    private Switch aSwitch;

    public static ParameterFragment newInstance(IEtudiant etudiant) {
        ParameterFragment pf = new ParameterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ETUDIANT_KEY, etudiant);
        pf.setArguments(bundle);
        return pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        etudiant = (IEtudiant) getArguments().getSerializable(ETUDIANT_KEY);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parameter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.parametres));
        Button b1 = getActivity().findViewById(R.id.buttonChangeEtudiant);
        b1.setOnClickListener(this);
        Button b2 = getActivity().findViewById(R.id.buttonChangeUEs);
        b2.setOnClickListener(this);
        aSwitch = getActivity().findViewById(R.id.switchActivateNotif);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        aSwitch.setChecked(sharedPref.getBoolean(getString(R.string.notif_file), true));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.notif_file), b);
                editor.apply();
                if(b){
                    AlarmManagerEvent.getInstance(getContext()).createNotifications(etudiant.getCalendar().getAllEvents(), 15 * 60 * 1000);
                }else{
                    AlarmManagerEvent.getInstance(getContext()).cancelAlarm(etudiant.getCalendar().getAllEvents());
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.buttonChangeEtudiant:
                AlarmManagerEvent.getInstance(getContext()).cancelAlarm(etudiant.getCalendar().getAllEvents());
                getContext().deleteFile(getString(R.string.etudiant_saver));
                intent = new Intent(getContext(), LoginActivity.class);
                break;
            case R.id.buttonChangeUEs:
                AlarmManagerEvent.getInstance(getContext()).cancelAlarm(etudiant.getCalendar().getAllEvents());
                getContext().deleteFile(getString(R.string.etudiant_saver));
                intent = new Intent(getContext(), UeChooserActivity.class);
                intent.putExtra("etudiant", etudiant);
                break;
        }
        if(intent != null){
            startActivity(intent);
            getActivity().finish();
        }
    }

}
