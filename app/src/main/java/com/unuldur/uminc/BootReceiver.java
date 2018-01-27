package com.unuldur.uminc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intentAlarm = new Intent(context, NoteUpdateReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intentAlarm, 0);
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0,AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }
}
