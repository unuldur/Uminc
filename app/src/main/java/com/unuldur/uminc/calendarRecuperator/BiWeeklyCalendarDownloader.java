package com.unuldur.uminc.calendarRecuperator;

import android.content.Context;

import com.unuldur.uminc.R;
import com.unuldur.uminc.connection.IConnection;
import com.unuldur.uminc.model.IEvent;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import biweekly.Biweekly;
import biweekly.ICalendar;

/**
 * Created by Unuldur on 14/12/2017.
 */

public class BiWeeklyCalendarDownloader implements ICalendarDownloader {
    private IConnection connection;
    private Context context;

    public BiWeeklyCalendarDownloader(IConnection connection, Context context) {
        this.connection = connection;
        this.context = context;
    }

    @Override
    public List<IEvent> getEventsFromAdresses(List<String> adresses) {
        return null;
    }

    @Override
    public List<IEvent> getEventsFromAdress(String address) {
        String ics = connection.getPage(address,
                context.getString(R.string.caldavzap_login),
                context.getString(R.string.caldavzap_password));
        if(ics == null) return null;
        StringReader sin = new StringReader(ics);
        long time = GregorianCalendar.getInstance().getTimeInMillis();
        System.out.println("start");
        ICalendar ical = Biweekly.parse(ics).first();
        System.out.println("Finish in " + (GregorianCalendar.getInstance().getTimeInMillis() - time));
        return null;
    }
}
