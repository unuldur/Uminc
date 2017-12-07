package com.unuldur.uminc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.UE;

import java.util.Calendar;

/**
 * Created by Unuldur on 07/12/2017.
 */

public class EtudiantUeChooserAdapter extends ArrayAdapter<UE> {

    private IEtudiant etudiant;
    private Context context;
    public EtudiantUeChooserAdapter(@NonNull Context context, int resource, IEtudiant etudiant) {
        super(context, 0, etudiant.getUEs());
        this.context = context;
        this.etudiant = etudiant;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ue_selector,parent, false);
        }
        final UE ue = etudiant.getUEs().get(position);

        TextView tv = convertView.findViewById(R.id.ueTitle);
        tv.setText(String.format("%s - %s", ue.getId(), ue.getName()));

        final CheckBox cb = convertView.findViewById(R.id.ueCheck);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked()){
                    etudiant.getActualUEs().add(ue);
                }else{
                    etudiant.getActualUEs().remove(ue);
                }
            }
        });

        return convertView;
    }
}
