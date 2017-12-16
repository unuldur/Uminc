package com.unuldur.uminc.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Unuldur on 01/12/2017.
 */

public class SimpleEvent implements IEvent, Serializable {
    private String title;
    private String localisation;
    private Calendar startDate;
    private Calendar endDate;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SimpleEvent)) return false;
        SimpleEvent event = (SimpleEvent) obj;
        if(!event.startDate.equals(startDate)) return false;
        if(!event.endDate.equals(endDate)) return false;
        return true;
    }

    public SimpleEvent(String title, String localisation, Calendar startDate, Calendar endDate) {
        this.title = title;
        this.localisation = localisation;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public int getYear() {
        return startDate.get(Calendar.YEAR);
    }

    @Override
    public int getWeek() {
        return startDate.get(Calendar.WEEK_OF_YEAR);
    }

    @Override
    public Calendar getStartDate() {
        return startDate;
    }

    @Override
    public Calendar getEndDate() {
        return endDate;
    }

    @Override
    public String getTitre() {
        return title;
    }

    @Override
    public String getLocalisation() {
        return localisation;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        SimpleDateFormat sbf = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        sb.append(sbf.format(startDate.getTime()));
        sb.append(" : ");
        sb.append(title);
        sb.append(" : ");
        sb.append(sbf.format(endDate.getTime()));
        return sb.toString();
    }
}
