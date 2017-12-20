package com.unuldur.uminc.calendarRecuperator;

import android.util.Log;

import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.IEvent;
import com.unuldur.uminc.model.SimpleCalendar;
import com.unuldur.uminc.model.UE;

import java.util.List;

/**
 * Created by Unuldur on 17/12/2017.
 */

public class SimpleCalendarRecuperator implements ICalendarRecuperator {

    private ICalendarDownloader downloader;

    public SimpleCalendarRecuperator(ICalendarDownloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public ICalendar getCalendar(IEtudiant e) {
        ICalendar cal = new SimpleCalendar();
        for (String adress: e.getCalendar().getAdress()) {
            if(!addAdresse(adress, cal, e))
            {
                return null;
            }
        }
        cal.setAdress(e.getCalendar().getAdress());
        return cal;
    }

    private boolean addAdresse(String adress, ICalendar cal, IEtudiant e){
        List<IEvent> events = downloader.getEventsFromAdress(adress);
        if(events == null) return false;
        Log.d("CalendarRecuperator", adress);
        for(IEvent event: events){
            Log.d("CalendarRecuperator", event.getTitre());
            String[] eventSplit = event.getTitre().split("-");
            if (eventSplit.length < 3) continue;
            for(UE ue :e.getActualUEs()){
                String gr = "";
                if(ue.getGroupe().length() >= 3){
                    gr = String.valueOf(ue.getGroupe().toCharArray()[2]);
                }
                if(eventSplit[0].contains(ue.getId()) &&
                        (eventSplit[1].contains("Cours") ||
                                eventSplit[1].contains("Examen") ||
                                eventSplit[1].equals("TD") ||
                                eventSplit[1].equals("TME") ||
                                eventSplit[1].contains(gr))){
                    event.setUE(ue);
                    cal.addEvent(event);
                }
            }
        }
        return true;
    }
}
