package com.unuldur.uminc.calendarRecuperator;

import android.os.AsyncTask;

import com.unuldur.uminc.model.IEvent;

import java.util.List;

/**
 * Created by Unuldur on 14/12/2017.
 */

public class TaskCalendarDownloader extends AsyncTask<String, Void, List<IEvent>> {
    private ICalendarDownloader scd;

    public TaskCalendarDownloader(ICalendarDownloader scd) {
        this.scd = scd;
    }

    @Override
    protected List<IEvent> doInBackground(String... strings) {
        return scd.getEventsFromAdress(strings[0]);
    }
}
