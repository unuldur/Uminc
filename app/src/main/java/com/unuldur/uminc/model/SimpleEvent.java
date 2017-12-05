package com.unuldur.uminc.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Unuldur on 01/12/2017.
 */

public class SimpleEvent implements IEvent {
    private String title;
    private String localisation;
    private Calendar startDate;
    private Calendar endDate;

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
}
