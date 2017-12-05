package com.unuldur.uminc;


import android.graphics.Color;
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
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IEvent;
import com.unuldur.uminc.model.SimpleCalendar;
import com.unuldur.uminc.model.SimpleEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int WEEKS_IN_YEARS = 52;
    private static final int HEIGH = 30;
    private ICalendar calendar;
    private GridLayout grdl;
    private int nbChildBaseCount;
    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = new SimpleCalendar();

        calendar.addEvent(new SimpleEvent("titre 1", "15-15 201",
                new GregorianCalendar(2017,11,4,8,30,0),
                new GregorianCalendar(2017,11,4,10,30,0)));
        calendar.addEvent(new SimpleEvent("titre 1 - 2", "15-15 201",
                new GregorianCalendar(2017,11,4,10,45,0),
                new GregorianCalendar(2017,11,4,12,45,0)));
        calendar.addEvent(new SimpleEvent("titre 2 - 1", "15-15 201",
                new GregorianCalendar(2017,11,4,13,45,0),
                new GregorianCalendar(2017,11,4,15,45,0)));
        calendar.addEvent(new SimpleEvent("titre 2 - 2", "15-15 201",
                new GregorianCalendar(2017,11,4,16,0,0),
                new GregorianCalendar(2017,11,4,18,0,0)));
        calendar.addEvent(new SimpleEvent("titre prev", "15-15 201",
                new GregorianCalendar(2017,10,28,16,0,0),
                new GregorianCalendar(2017,10,28,18,0,0)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    private void initGrid(GridLayout gridLayout, int weeks, int years){
        List<IEvent> events = calendar.getWeeksEvents(weeks, years);
        int childCount =  gridLayout.getChildCount();
        for (int i = childCount - 1; i >= nbChildBaseCount; i--) {
            gridLayout.removeViewAt(i);
        }
        if(events == null)
            return;
        for(int i = 1; i < gridLayout.getColumnCount(); i++){
            for (int j = 1; j < gridLayout.getRowCount(); j++) {
                TextView s = new TextView(getContext());
                s.setHeight(HEIGH);
                GridLayout.LayoutParams glp = new GridLayout.LayoutParams();
                glp.rowSpec = GridLayout.spec(j);
                glp.columnSpec = GridLayout.spec(i);
                gridLayout.addView(s, glp);
            }
        }
        for(IEvent e:events) {
            Button b = new Button(getContext());
            b.setBackgroundColor(Color.GRAY);
            b.setText(String.format("%s\n%s", e.getTitre(), e.getLocalisation()));
            Calendar cStart = e.getStartDate();
            Calendar cEnd = e.getEndDate();
            int indexrow = (cStart.get(Calendar.HOUR_OF_DAY) - 8) * 4 + (cStart.get(Calendar.MINUTE) / 15) + 1;
            int indexrowend = (cEnd.get(Calendar.HOUR_OF_DAY) - 8) * 4 + (cEnd.get(Calendar.MINUTE) / 15) + 1;
            b.setHeight(HEIGH * (indexrowend - indexrow + 1));
            GridLayout.Spec row = GridLayout.spec(indexrow, indexrowend - indexrow);
            GridLayout.Spec col = GridLayout.spec(cStart.get(Calendar.DAY_OF_WEEK));
            Log.d("Calendar", "" + cStart.get(Calendar.DAY_OF_WEEK));
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, col);
            gridLayout.addView(b, gridLayoutParam);

        }
        Log.d("Calendar", "============================");
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

        grdl = getView().findViewById(R.id.grid_calendar);
        nbChildBaseCount = grdl.getChildCount();
        initGrid(grdl, 49, 2017);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //initGrid(grdl, 48, 2017);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("grdl_init", false);
    }
}
