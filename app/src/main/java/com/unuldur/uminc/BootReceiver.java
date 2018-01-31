package com.unuldur.uminc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.unuldur.uminc.model.IEtudiant;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by Unuldur on 24/01/2018.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String filename = context.getString(R.string.etudiant_saver);
        FileInputStream outputStream;
        IEtudiant etu;
        try {
            outputStream = context.openFileInput(filename);
            ObjectInputStream oos = new ObjectInputStream(outputStream);
            etu = (IEtudiant) oos.readObject();
            outputStream.close();
        } catch (Exception e) {
            etu = null;
        }
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && etu != null) {
            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intentAlarm = new Intent(context, NetworkChangedReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intentAlarm, 0);
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,AlarmManager.INTERVAL_DAY, alarmIntent);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            if(sharedPref.getBoolean(context.getString(R.string.notif_file), true)) {
                Log.d("Boot", "Shared preference true");
                AlarmManagerEvent.getInstance(context).createNotifications(etu.getCalendar().getAllEvents(), 15*1000*60);
            }
        }
    }
}
