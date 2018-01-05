package com.unuldur.uminc;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.thebluealliance.spectrum.SpectrumDialog;
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
        final Button button = convertView.findViewById(R.id.buttonColor);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpectrumDialog sd = new SpectrumDialog.Builder(getContext())
                        .setColors(R.array.colors_calendars)
                        .setSelectedColor(ue.getColor())
                        .setDismissOnColorSelected(true)
                        .setOutlineWidth(2)
                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                if (positiveResult) {
                                    button.setBackgroundColor(color);
                                    ue.setColor(color);
                                }
                            }
                        }).build();

                FragmentActivity a = (FragmentActivity)context;
                sd.show(a.getSupportFragmentManager(), "Colors");
            }
        });
        cb.setChecked(etudiant.getActualUEs().contains(ue));
        if(cb.isChecked()){
            button.setVisibility(View.VISIBLE);
            button.setBackgroundColor(ue.getColor());
        }else{
            button.setVisibility(View.GONE);
        }
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked()){
                    etudiant.getActualUEs().add(ue);
                    button.setVisibility(View.VISIBLE);
                    button.setBackgroundColor(ue.getColor());
                }else{
                    etudiant.getActualUEs().remove(ue);
                    button.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }
}
