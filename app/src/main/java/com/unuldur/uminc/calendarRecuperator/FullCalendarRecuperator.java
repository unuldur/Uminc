package com.unuldur.uminc.calendarRecuperator;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.unuldur.uminc.R;
import com.unuldur.uminc.connection.Connection;
import com.unuldur.uminc.connection.IConnection;
import com.unuldur.uminc.model.Etudiant;
import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.IEvent;
import com.unuldur.uminc.model.SimpleCalendar;
import com.unuldur.uminc.model.UE;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by julie on 11/12/2017.
 */

public class FullCalendarRecuperator implements ICalendarRecuperator {
    private IConnection connection;
    private Context context;
    private ICalendarDownloader downloader;
    public FullCalendarRecuperator(Context context) {
        connection = new Connection(context);
        this.context = context;
        downloader = new SimpleCalendarDownloader(connection, context);
    }

    public FullCalendarRecuperator(IConnection connection, Context context) {
        this.connection = connection;
        this.context = context;
        downloader = new SimpleCalendarDownloader(connection, context);
    }

    @Override
    public ICalendar getCalendar(IEtudiant e) {
        List<String> globalAdresses = getGlobalAdress();
        if(globalAdresses == null) return null;
        List<String> allAdresses = getAllAdress(globalAdresses);
        if(allAdresses == null) return null;
        List<String> adressOk = new ArrayList<>();
        ICalendar cal = new SimpleCalendar();
        for (String adress: allAdresses) {
            if(testAdresse(adress, cal, e)){
                adressOk.add(adress);
            }
        }
        return cal;
    }

    private boolean testAdresse(String adress, ICalendar cal, IEtudiant e){
        List<IEvent> events = downloader.getEventsFromAdress(adress);
        if(events == null) return false;
        boolean ok = false;
        Log.d("CalendarRecuperator", adress);
        for(IEvent event: events){
            Log.d("CalendarRecuperator", event.getTitre());
            for(UE ue :e.getActualUEs()){
                String[] eventSplit = event.getTitre().split("-");
                String gr = "";
                if(ue.getGroupe().getBytes().length >= 3){
                    gr = String.valueOf(ue.getGroupe().getBytes()[2]);
                }
                Log.d("CalendarRecuperator", gr);
                if(eventSplit[0].contains(ue.getId()) &&
                        (eventSplit[1].contains("Cours") ||
                                eventSplit[1].contains("Examen") ||
                                eventSplit[1].contains(gr))){
                    cal.addEvent(event);
                    ok = true;
                }
            }
        }
        return ok;
    }

    public List<String> getGlobalAdress(){
        String configCaldavzap = connection.getPage(context.getString(R.string.caldavzap_config_adress));
        String[] lines = configCaldavzap.split("\n");
        List<String> adress = new ArrayList<>();
        boolean checkLineNext = false;
        for (String line:lines) {
            if(checkLineNext){
                if(line.contains("];")){
                    break;
                }
                String[] text = line.split("[, ]");
                adress.add(text[1].subSequence(1, text[1].length() - 1).toString());
            }
            if(line.equals("var globalAccountSettings=[")){
                checkLineNext = true;
            }
        }
        return adress;
    }

    public List<String> getAllAdress(List<String> globalAdress){
        List<String> adresss = new ArrayList<>();
        for (String adress: globalAdress) {
            String res = connection.getPage(adress,"PROPFIND", context.getString(R.string.caldavzap_login), context.getString(R.string.caldavzap_password),1);
            if(res == null) return null;
            try {
               adresss.addAll(getAdress(res));
            } catch (XmlPullParserException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
        return adresss;
    }

    private List<String> getAdress(String xml) throws XmlPullParserException, IOException {
        XmlPullParser xpp = Xml.newPullParser();
        xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xpp.setInput(new StringReader(xml));
        xpp.nextTag();
        List<String> adress = new ArrayList<>();
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if(xpp.getName().equals("response")){
                String address = parseResponse(xpp);
                if(address != null){
                    adress.add(context.getString(R.string.caldavzap_base_adress) + address);
                }
            }else{
                skip(xpp);
            }

        }
        return adress;
    }

    private String parseResponse(XmlPullParser parser) throws XmlPullParserException, IOException {
        String res = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals("href")){
                if (parser.next() == XmlPullParser.TEXT) {
                    res = parser.getText();
                    parser.nextTag();
                }
            }else if(parser.getName().equals("propstat")){
                if(!parsePropStat(parser))
                    res = null;
            }else{
                skip(parser);
            }

        }
        return res;
    }

    private boolean parsePropStat(XmlPullParser parser) throws XmlPullParserException, IOException{
        boolean res = false;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("prop")){
                res = parseType(parser);
            }else{
                skip(parser);
            }

        }
        return res;
    }

    private boolean parseType(XmlPullParser parser) throws XmlPullParserException, IOException{
        boolean res = false;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("resourcetype")){
                res = parseResourceType(parser);
            }else{
                skip(parser);
            }

        }
        return res;
    }

    private boolean parseResourceType(XmlPullParser parser) throws XmlPullParserException, IOException{
        boolean res = false;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            String[] tmps = name.split(":");
            if(tmps.length > 1 && tmps[1].equals("calendar")){
                res = true;
                parser.nextTag();
            }else{
                skip(parser);
            }

        }
        return res;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
