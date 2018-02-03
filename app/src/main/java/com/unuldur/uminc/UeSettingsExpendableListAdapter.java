package com.unuldur.uminc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.thebluealliance.spectrum.SpectrumDialog;
import com.unuldur.uminc.model.UE;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Unuldur on 03/02/2018.
 */

public class UeSettingsExpendableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<UE> expandableListTitle;
    private HashMap<UE, List<String>> expandableListDetail;

    public UeSettingsExpendableListAdapter(Context context, List<UE> expandableListTitle, HashMap<UE, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        UE ue = (UE) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.ue_settings_item, null);
        }
        TextView horaireTextView = convertView.findViewById(R.id.text_settings_ue_item);
        horaireTextView.setText(expandedListText);
        final CheckBox checkBox = convertView.findViewById(R.id.checkBox_settings_item);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String id = ue.toString()+"/"+expandedListText;
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(sharedPref.getBoolean(id, true));
        //Log.d("Adaptateur", "4XAN1 - Anglais/4XAN1-TP-Anglais : lun. 08:30 - lun. 12:30 " + sharedPref.getBoolean("4XAN1 - Anglais/4XAN1-TP-Anglais : lun. 08:30 - lun. 12:30", true) +  " " + sharedPref.contains("4XAN1 - Anglais/4XAN1-TP-Anglais : lun. 08:30 - lun. 12:30"));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(id, b);
                editor.apply();
                Log.d("Adaptateur", "change " + id + " " + sharedPref.getBoolean(id, true) +  " " + sharedPref.contains(id));
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final UE ue = (UE) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.ue_settings_group, null);
        }
        TextView ueTextView = convertView.findViewById(R.id.text_settings_ue_group);
        ueTextView.setTypeface(null, Typeface.BOLD);
        ueTextView.setText(ue.toString());
        final Button ueButton = convertView.findViewById(R.id.button_color_settings);
        ueButton.setBackgroundColor(ue.getColor());
        ueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpectrumDialog sd = new SpectrumDialog.Builder(context)
                        .setColors(R.array.colors_calendars)
                        .setSelectedColor(ue.getColor())
                        .setDismissOnColorSelected(true)
                        .setOutlineWidth(2)
                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                if (positiveResult) {
                                    ueButton.setBackgroundColor(color);
                                    ue.setColor(color);
                                }
                            }
                        }).build();

                FragmentActivity a = (FragmentActivity)context;
                sd.show(a.getSupportFragmentManager(), "Colors");
            }
        });
        ueButton.setFocusable(false);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return false;
    }

}
