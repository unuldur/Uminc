package com.unuldur.uminc.model;

import java.util.List;

/**
 * Created by Unuldur on 01/12/2017.
 */

public interface ICalendar {
    void addEvent(IEvent event);
    List<IEvent> getAllEvents();
    List<IEvent> getWeeksEvents(int weeks, int year);
    void setAdress(List<String> adress);
    List<String> getAdress();
}
