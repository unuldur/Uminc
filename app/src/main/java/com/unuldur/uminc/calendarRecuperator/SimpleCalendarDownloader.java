package com.unuldur.uminc.calendarRecuperator;

import android.content.Context;

import com.unuldur.uminc.R;
import com.unuldur.uminc.connection.IConnection;
import com.unuldur.uminc.model.IEvent;
import com.unuldur.uminc.model.SimpleEvent;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Summary;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by julie on 12/12/2017.
 */

public class SimpleCalendarDownloader implements ICalendarDownloader {
    private IConnection connection;
    private Context context;
    public SimpleCalendarDownloader(IConnection connection, Context context) {
        this.connection = connection;
        this.context = context;
    }

    @Override
    public List<IEvent> getEventsFromAdresses(List<String> adresses) {
        List<IEvent> events = new ArrayList<>();
        for (String adress:adresses) {
            events.addAll(getEventsFromAdress(adress));
        }
        return events;
    }

    @Override
    public List<IEvent> getEventsFromAdress(String address) {
        String ics = connection.getPage(address,
                context.getString(R.string.caldavzap_login),
                context.getString(R.string.caldavzap_password));
        if(ics == null) return null;
        StringReader sin = new StringReader(ics);
        List<IEvent> events = new ArrayList<>();
        CalendarBuilder builder = new CalendarBuilder();

        try {
            Calendar calendar = builder.build(sin);
            for (Object c: calendar.getComponents("VEVENT")) {
                Component comp = (Component)c;
                Summary name = (Summary)comp.getProperty("SUMMARY");

                Location loc = (Location)comp.getProperty("LOCATION");
                DtStart dateDeb = (DtStart)comp.getProperty("DTSTART");
                DtEnd dateFin = (DtEnd)comp.getProperty("DTEND");
                java.util.Calendar deb = java.util.Calendar.getInstance();
                deb.setTime(dateDeb.getDate());
                java.util.Calendar fin = java.util.Calendar.getInstance();
                fin.setTime(dateFin.getDate());
                String locs = "";
                if(loc == null){
                    locs = "Inconnue";
                }else{
                    locs = loc.getValue();
                }
                IEvent ev = new SimpleEvent(name.getValue(), locs, deb , fin);
                events.add(ev);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }
}
