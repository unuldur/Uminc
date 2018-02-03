package com.unuldur.uminc.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Unuldur on 01/12/2017.
 */

public class SimpleCalendar implements ICalendar, Serializable {
    private Map<String, List<IEvent>> maps = new HashMap<>();
    private Map<UE, Map<String, List<IEvent>>> horaireMap = new HashMap<>();

    private List<String> adress = new ArrayList<>();


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

        Map<String, List<IEvent>> horaires = horaireMap.get(event.getUE());
        if(horaires == null){
            horaires = new HashMap<>();
        }
        String horaire = event.getHoraire();
        List<IEvent> events = horaires.get(horaire);
        if(events == null){
            events = new ArrayList<>();
        }
        if(events.contains(event)){
            events.remove(event);
        }
        events.add(event);
        horaires.put(horaire, events);
        horaireMap.put(event.getUE(), horaires);
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
    public List<String> getHoraires(UE ue) {
        if(horaireMap.get(ue) == null) return null;
        return new ArrayList<>(horaireMap.get(ue).keySet());
    }

    @Override
    public List<IEvent> getEventsByHoraire(UE ue, String horaire) {
        return horaireMap.get(ue).get(horaire);
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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SimpleCalendar)) return false;
        SimpleCalendar cal = (SimpleCalendar)obj;
        if(!cal.maps.equals(maps)) return false;
        return true;
    }
}
