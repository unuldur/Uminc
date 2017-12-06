package com.unuldur.uminc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unuldur.uminc.model.IEvent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by julie on 06/12/2017.
 */

public class EventAdpater extends ArrayAdapter<Calendar> {
    private Context context;
    private List<Calendar> values;

    public EventAdpater(@NonNull Context context, int resource, @NonNull List<Calendar> objects) {
        super(context, resource, objects);
        this.context = context;
        values = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = new TextView(context);
        Calendar date = values.get(position);
        label.setText(String.format("Semaine %d: %d/%d/%d", date.get(Calendar.WEEK_OF_YEAR),
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.YEAR)));
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = new TextView(context);
        Calendar date = values.get(position);
        label.setText(String.format("Semaine %d: %d/%d/%d", date.get(Calendar.WEEK_OF_YEAR),
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.YEAR)));
        label.setPadding(5,5,5,5);
        return label;
    }
}
