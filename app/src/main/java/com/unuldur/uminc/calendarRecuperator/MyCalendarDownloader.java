package com.unuldur.uminc.calendarRecuperator;

import android.content.Context;

import com.unuldur.uminc.R;
import com.unuldur.uminc.connection.IConnection;
import com.unuldur.uminc.model.IEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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
            while (line != null) {
                if(line.equals("BEGIN:VEVENT")){
                    readVevent = true;
                }
                if(line.equals("END:VEVENT")){
                    readVevent = false;
                    events.addAll(getEvent(summary,location, dtstart, dtend, rrule));
                    summary = null;
                    dtstart = null;
                    dtend = null;
                    location = null;
                    rrule = null;
                }
                if(readVevent){
                    if(line.contains("RRULE")){
                        rrule = line.split(":")[1];
                    }
                    if(line.contains("SUMMARY")){
                        summary = line.split(":")[1];
                    }
                    if(line.contains("LOCATION")){
                        location = line.split(":")[1];
                    }
                    if(line.contains("DTSTART")){
                        dtstart = line.split(":")[1];
                    }
                    if(line.contains("DTEND")){
                        dtend = line.split(":")[1];
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            return null;
        }

        System.out.println("Finish in " + (GregorianCalendar.getInstance().getTimeInMillis() - time));
        return events;
    }

    private List<IEvent> getEvent(String summary, String location, String dtstart, String dtend, String rrule){

        return new ArrayList<>();
    }


}
