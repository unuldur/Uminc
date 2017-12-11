package com.unuldur.uminc.calendarRecuperator;

import android.content.Context;
import android.util.Log;

import com.unuldur.uminc.R;
import com.unuldur.uminc.connection.Connection;
import com.unuldur.uminc.connection.IConnection;
import com.unuldur.uminc.model.Etudiant;
import com.unuldur.uminc.model.ICalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julie on 11/12/2017.
 */

public class FullCalendarRecuperator implements ICalendarRecuperator {
    private IConnection connection;
    private Context context;
    public FullCalendarRecuperator(Context context) {
        connection = new Connection(context);
        this.context = context;
    }

    public FullCalendarRecuperator(IConnection connection, Context context) {
        this.connection = connection;
        this.context = context;
    }

    @Override
    public ICalendar getCalendar(Etudiant e) {

        return null;
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
        for (String adress: globalAdress) {
            String res = connection.getPage(adress,"PROPFIND", context.getString(R.string.caldavzap_login), context.getString(R.string.caldavzap_password),1);
            res += "aie";
        }
        return null;
    }
}
