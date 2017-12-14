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
import net.fortuna.ical4j.model.DateList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.util.CompatibilityHints;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by julie on 12/12/2017.
 */

public class Ical4jCalendarDownloader implements ICalendarDownloader {
    private IConnection connection;
    private Context context;
    private CalendarBuilder builder = new CalendarBuilder();
    public Ical4jCalendarDownloader(IConnection connection, Context context) {
        this.connection = connection;
        this.context = context;

        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
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
        try {

            long time = GregorianCalendar.getInstance().getTimeInMillis();
            System.out.println("start");
            Calendar calendar = builder.build(sin);
            System.out.println("Finish in " + (GregorianCalendar.getInstance().getTimeInMillis() - time));
            for (Object c: calendar.getComponents("VEVENT")) {
                Component comp = (Component) c;
                Summary name = (Summary) comp.getProperty("SUMMARY");
                Location loc = (Location) comp.getProperty("LOCATION");
                DtStart dateDeb = (DtStart) comp.getProperty("DTSTART");
                DtEnd dateFin = (DtEnd) comp.getProperty("DTEND");
                RRule rules = (RRule) comp.getProperty("RRULE");
                if(rules != null){
                    Recur recur = rules.getRecur();
                    DateList dl = recur.getDates(dateDeb.getDate(), recur.getUntil(), Value.DATE);
                    int heureDeb = dateDeb.getDate().getHours();
                    int minDeb = dateDeb.getDate().getMinutes();
                    int heureFin = dateFin.getDate().getHours();
                    int minFin = dateFin.getDate().getMinutes();
                    for(Object odatedeb : dl){
                        Date datedeb = (Date) odatedeb;
                        datedeb.setHours(heureDeb);
                        datedeb.setMinutes(minDeb);
                        Date datefin = (Date)datedeb.clone();
                        datefin.setHours(heureFin);
                        datefin.setMinutes(minFin);
                        events.add(getEvent(name, loc, dateDeb.getDate(), dateFin.getDate()));
                    }
                }else{
                    events.add(getEvent(name, loc, dateDeb.getDate(), dateFin.getDate()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    private IEvent getEvent(Summary name, Location loc, Date ddeb, Date dfin){
        java.util.Calendar deb = java.util.Calendar.getInstance();
        deb.setTime(ddeb);
        java.util.Calendar fin = java.util.Calendar.getInstance();
        fin.setTime(dfin);
        String locs;
        if (loc == null) {
            locs = "Inconnue";
        } else {
            locs = loc.getValue();
        }
        return new SimpleEvent(name.getValue(), locs, deb, fin);
    }
}
