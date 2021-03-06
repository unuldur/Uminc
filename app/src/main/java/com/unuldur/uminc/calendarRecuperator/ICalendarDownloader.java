package com.unuldur.uminc.calendarRecuperator;

import com.unuldur.uminc.model.IEvent;

import java.util.List;

/**
 * Created by julie on 11/12/2017.
 */

public interface ICalendarDownloader {
    List<IEvent> getEventsFromAdresses(List<String> adresses);
    List<IEvent> getEventsFromAdress(String address);
}
