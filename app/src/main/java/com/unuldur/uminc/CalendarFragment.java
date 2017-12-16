package com.unuldur.uminc;


import android.app.Dialog;
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
import com.unuldur.uminc.model.IEtudiant;
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
    private GridLayout grdl;
    private int nbChildBaseCount;
    private static final String ETUDIANT_KEY = "etudiant";
    IEtudiant etudiant;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(IEtudiant etudiant) {
        CalendarFragment cf = new CalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ETUDIANT_KEY, etudiant);
        cf.setArguments(bundle);
        return cf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        etudiant = (IEtudiant) getArguments().getSerializable(ETUDIANT_KEY);
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    private void initGrid(GridLayout gridLayout, int weeks, int years){
        List<IEvent> events = etudiant.getCalendar().getWeeksEvents(weeks, years);
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
        for(final IEvent e:events) {
            Button b = new Button(getContext());
            b.setBackgroundColor(Color.GRAY);
            b.setText(String.format("%s\n%s", e.getTitre(), e.getLocalisation()));
            Calendar cStart = e.getStartDate();
            Calendar cEnd = e.getEndDate();
            int indexrow = (cStart.get(Calendar.HOUR_OF_DAY) - 8) * 4 + (cStart.get(Calendar.MINUTE) / 15) + 1;
            int indexrowend = (cEnd.get(Calendar.HOUR_OF_DAY) - 8) * 4 + (cEnd.get(Calendar.MINUTE) / 15) + 1;
            b.setHeight(HEIGH * (indexrowend - indexrow + 1));
            b.setWidth(400);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog d = new PopupEvent(getContext(), e);
                    d.show();
                }
            });
            GridLayout.Spec row = GridLayout.spec(indexrow, indexrowend - indexrow);
            GridLayout.Spec col = GridLayout.spec(cStart.get(Calendar.DAY_OF_WEEK) - 1);
            Log.d("Calendar", "" + cStart.get(Calendar.DAY_OF_WEEK));
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, col);
            gridLayoutParam.setMargins(1,1,1,1);
            gridLayout.addView(b, gridLayoutParam);

        }
        Log.d("Calendar", "============================");
    }

    private List<Calendar> getWeeks(){
        int current_week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        List<Calendar> weeks = new ArrayList<>();
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
            weeks.add(cal);
        }
        return weeks;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Spinner spinner = getView().findViewById(R.id.spinnerCalendar);
        final List<Calendar> weeks = getWeeks();
        EventAdpater adapter = new EventAdpater(getContext(), android.R.layout.simple_spinner_item, weeks);
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
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Calendar cal = (Calendar)adapterView.getItemAtPosition(i);
        initGrid(grdl, cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.YEAR));
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
