package com.unuldur.uminc;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unuldur.uminc.calendarRecuperator.ICalendarRecuperator;
import com.unuldur.uminc.calendarRecuperator.MyCalendarDownloader;
import com.unuldur.uminc.calendarRecuperator.SimpleCalendarRecuperator;
import com.unuldur.uminc.connection.Connection;
import com.unuldur.uminc.model.DbufrConnection;
import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IDbufrConnection;
import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.IEvent;
import com.unuldur.uminc.model.SimpleCalendar;
import com.unuldur.uminc.model.SimpleEvent;
import com.unuldur.uminc.model.UE;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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
    private Spinner spinner;
    private static final String ETUDIANT_KEY = "etudiant";
    private IEtudiant etudiant;
    private SyncroniseCalendar syncroniseCalendar;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        etudiant = (IEtudiant) getArguments().getSerializable(ETUDIANT_KEY);
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    private void initGrid(GridLayout gridLayout, int weeks, int years){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        List<IEvent> events = etudiant.getCalendar().getWeeksEvents(weeks, years);
        int childCount =  gridLayout.getChildCount();
        for (int i = childCount - 1; i >= nbChildBaseCount; i--) {
            gridLayout.removeViewAt(i);
        }
        if(events == null)
            return;
        int pixels = (int) (getResources().getDimension(R.dimen.collumn_calendar_size)+ 0.5f);
        int height = (int) (getResources().getDimension(R.dimen.row_callendar_size));
        for (int j = 1; j < gridLayout.getRowCount(); j++) {
            TextView s = new TextView(getContext());
            s.setHeight(height);
            GridLayout.LayoutParams glp = new GridLayout.LayoutParams();
            glp.rowSpec = GridLayout.spec(j);
            glp.columnSpec = GridLayout.spec(0);
            gridLayout.addView(s, glp);
        }
        for(final IEvent e:events) {
            String horaire = e.getHoraire();
            if(!sharedPref.getBoolean(e.getUE().toString() + "/" + horaire, true)){
                continue;
            }
            Button b = new AppCompatButton(gridLayout.getContext());
            if(e.getUE() != null)
                b.setBackgroundColor(e.getUE().getColor());
            else
                b.setBackgroundColor(Color.GRAY);
            b.setText(String.format("%s\n%s", e.getTitre(), e.getLocalisation()));
            Calendar cStart = e.getStartDate();
            Calendar cEnd = e.getEndDate();
            int indexrow = (cStart.get(Calendar.HOUR_OF_DAY) - 8) * 4 + (cStart.get(Calendar.MINUTE) / 15) + 1;
            int indexrowend = (cEnd.get(Calendar.HOUR_OF_DAY) - 8) * 4 + (cEnd.get(Calendar.MINUTE) / 15) + 1;
            b.setHeight(height * (indexrowend - indexrow));
            b.setWidth(pixels);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog d = new PopupEvent(getContext(), e);
                    d.show();
                }
            });
            GridLayout.Spec row = GridLayout.spec(indexrow, indexrowend - indexrow);
            GridLayout.Spec col = GridLayout.spec(cStart.get(Calendar.DAY_OF_WEEK) - 1);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, col);
            gridLayoutParam.setMargins(1,1,1,1);

            gridLayout.addView(b, gridLayoutParam);

        }
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

        spinner = view.findViewById(R.id.spinnerCalendar);
        final List<Calendar> weeks = getWeeks();
        EventAdpater adapter = new EventAdpater(getContext(), android.R.layout.simple_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(weeks.size() / 2);
        getActivity().setTitle("Calendrier");

        ImageButton button = view.findViewById(R.id.buttonNextWeek);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = spinner.getSelectedItemPosition();
                if(pos + 1 >= weeks.size()){
                    return;
                }
                spinner.setSelection(pos + 1);
            }
        });

        ImageButton button2 = view.findViewById(R.id.buttonPrevWeek);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = spinner.getSelectedItemPosition();
                if(pos < 0){
                    return;
                }
                spinner.setSelection(pos - 1);
            }
        });

        grdl = view.findViewById(R.id.grid_calendar);
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
        outState.putSerializable("etudiant", etudiant);
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
            if (syncroniseCalendar == null) {
                syncroniseCalendar = new SyncroniseCalendar();
                syncroniseCalendar.execute((Void) null);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SyncroniseCalendar  extends AsyncTask<Void, Void, ICalendar> {
        private final ICalendarRecuperator connection;
        SyncroniseCalendar() {
            connection = new SimpleCalendarRecuperator(new MyCalendarDownloader(new Connection(getContext()), getContext()));
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getContext(), "Synchronisation en cours...", Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected ICalendar doInBackground(Void... params) {
            return connection.getCalendar(etudiant);
        }

        @Override
        protected void onPostExecute(final ICalendar success) {
            syncroniseCalendar = null;

            if (success != null) {
                etudiant.addCalendar(success);
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
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                if(sharedPref.getBoolean(getContext().getString(R.string.notif_file), true)) {
                    AlarmManagerEvent.getInstance(getContext()).createNotifications(etudiant.getCalendar().getAllEvents(), 15 * 60 * 1000);
                }
                Toast.makeText(getContext(), "Synchronisation terminé", Toast.LENGTH_SHORT).show();
                Calendar cal = (Calendar)spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                initGrid(grdl, cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.YEAR));
            } else {
                Toast.makeText(getContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            syncroniseCalendar = null;
        }
    }
}
