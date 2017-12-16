package com.unuldur.uminc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Unuldur on 01/12/2017.
 */

public class SimpleCalendar implements ICalendar, Serializable {
    Map<String, List<IEvent>> maps = new HashMap<>();
    List<String> adress = new ArrayList<>();
    @Override
    public void addEvent(IEvent event) {
        String id = event.getWeek() +" "+ event.getYear();
        List<IEvent> simple = maps.get(id);
        if(simple == null){
            simple = new ArrayList<>();
        }
        if(simple.contains(event)){
            simple.remove(event);
        }
        simple.add(event);
        maps.put(id, simple);
    }

    @Override
    public List<IEvent> getAllEvents() {
        List<IEvent> event_final = new ArrayList<>();
        for (List<IEvent> events:maps.values()) {
            event_final.addAll(events);
        }
        return event_final;
    }

    @Override
    public List<IEvent> getWeeksEvents(int weeks, int year) {
        return maps.get(weeks +" "+ year);
    }

    @Override
    public void setAdress(List<String> adress) {
        this.adress = adress;
    }

    @Override
    public List<String> getAdress() {
        return adress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IEvent event: getAllEvents()) {
            sb.append("==========================\n");
            sb.append(event.toString());
        }
        return sb.toString();
    }
}
