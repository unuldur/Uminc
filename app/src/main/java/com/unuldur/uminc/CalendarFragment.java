package com.unuldur.uminc;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int WEEKS_IN_YEARS = 52;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }


    private List<String> getWeeks(){
        int current_week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        DateFormat sdf = SimpleDateFormat.getDateInstance();
        List<String> weeks = new ArrayList<>();
        for (int i = current_week - 10; i < current_week + 10; i++) {
            int j = i;

            Calendar cal = Calendar.getInstance();
            if(j > WEEKS_IN_YEARS){
                j = j - WEEKS_IN_YEARS;
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
            }
            if (j < 0)
            {
                cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
                j = WEEKS_IN_YEARS + j;
            }
            cal.set(Calendar.WEEK_OF_YEAR,j);
            cal.getTime();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            weeks.add("Semaine " + j + ": " + sdf.format(cal.getTime()));
        }
        return weeks;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Spinner spinner = getView().findViewById(R.id.spinnerCalendar);
        final List<String> weeks = getWeeks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(weeks.size() / 2);
        getActivity().setTitle("Calendrier");

        ImageButton button = getView().findViewById(R.id.buttonNextWeek);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = spinner.getSelectedItemPosition();
                if(pos + 1 >= weeks.size()){
                    return;
                }
                spinner.setSelection(pos + 1);
            }
        });

        ImageButton button2 = getView().findViewById(R.id.buttonPrevWeek);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = spinner.getSelectedItemPosition();
                if(pos < 0){
                    return;
                }
                spinner.setSelection(pos - 1);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
