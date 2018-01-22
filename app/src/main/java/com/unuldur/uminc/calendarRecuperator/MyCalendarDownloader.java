package com.unuldur.uminc.calendarRecuperator;

import android.content.Context;
import android.util.Log;

import com.unuldur.uminc.R;
import com.unuldur.uminc.connection.IConnection;
import com.unuldur.uminc.model.IEvent;
import com.unuldur.uminc.model.SimpleEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Unuldur on 14/12/2017.
 */

public class MyCalendarDownloader implements ICalendarDownloader {
    private IConnection connection;
    private Context context;

    public MyCalendarDownloader(IConnection connection, Context context) {
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
        BufferedReader reader = new BufferedReader(sin);
        List<IEvent> events = new ArrayList<>();
        String line;
        long time = GregorianCalendar.getInstance().getTimeInMillis();
        System.out.println("start");
        try {
            line = reader.readLine();
            boolean readVevent = false;
            String summary = null;
            String dtstart = null;
            String dtend = null;
            String location = null;
            String rrule = null;
            List<String> exdates = new ArrayList<>();
            boolean locationSuite = false;
            while (line != null) {
                if(line.equals("BEGIN:VEVENT")){
                    readVevent = true;
                }
                if(line.equals("END:VEVENT")){
                    readVevent = false;
                    try {
                        events.addAll(getEvents(summary,location, dtstart, dtend, rrule, exdates));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    summary = null;
                    dtstart = null;
                    dtend = null;
                    location = null;
                    rrule = null;
                    exdates = new ArrayList<>();
                }
                if(readVevent){
                    if(line.contains("RRULE")){
                        rrule = line.split(":")[1];
                    }
                    if(line.contains("SUMMARY")){
                        summary = line.split(":",2)[1];
                    }
                    if(line.contains("SEQUENCE")){
                        locationSuite = false;
                    }
                    if (locationSuite){
                        location += line.trim();
                    }
                    if(line.contains("LOCATION")){
                        String[] splits = line.split(":",2);
                        if(splits.length > 1){
                            location = splits[1];
                            locationSuite = true;
                        }
                    }
                    if(line.contains("DTSTART")){
                        dtstart = line.split(":")[1];
                    }
                    if(line.contains("DTEND")){
                        dtend = line.split(":")[1];
                    }
                    if(line.contains("EXDATE")){
                        exdates.add(line.split(":")[1]);
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("Finish in " + (GregorianCalendar.getInstance().getTimeInMillis() - time));
        return events;
    }

    private List<IEvent> getEvents(String summary, String location, String dtstart, String dtend, String rrule, List<String> exdates) throws ParseException {
        List<IEvent> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'kkmmss");
        Date datedeb;
        Date datedin;
        try {
            datedeb = sdf.parse(dtstart);
            datedin = sdf.parse(dtend);
        } catch (ParseException e) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
            datedeb = sdf2.parse(dtstart);
            datedin = sdf2.parse(dtend);
        }
        if(rrule != null){
            return getEventFromRrule(summary, location, datedeb, datedin, rrule, exdates);
        }
        result.add(getEvent(summary, location, datedeb, datedin));
        return result;
    }

    private List<IEvent> getEventFromRrule(String summary, String location, Date start, Date end, String rrule, List<String> exdates) throws ParseException {
        List<IEvent> result = new ArrayList<>();
        String[] rules = rrule.split(";");
        String suntil = rules[2].split("=")[1];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'kkmmss'Z'");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd'T'kkmmss");
        Date until = sdf.parse(suntil);
        int heureFin = end.getHours();
        int minFin = end.getMinutes();
        for(; start.before(until); start.setDate(start.getDate() + 7)){
            if(exdates.contains(sdf2.format(start))){
                continue;
            }
            Date rEnd = (Date)start.clone();
            rEnd.setHours(heureFin);
            rEnd.setMinutes(minFin);
            result.add(getEvent(summary, location, start, rEnd));
        }
        return result;
    }

    private IEvent getEvent(String name, String loc, Date ddeb, Date dfin){
        java.util.Calendar deb = java.util.Calendar.getInstance();
        deb.setTime(ddeb);
        java.util.Calendar fin = java.util.Calendar.getInstance();
        fin.setTime(dfin);
        if (loc == null) {
            loc = "Inconnue";
        }
        return new SimpleEvent(name, loc, deb, fin);
    }


}
