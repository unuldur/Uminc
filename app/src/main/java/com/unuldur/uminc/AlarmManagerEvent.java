package com.unuldur.uminc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.unuldur.uminc.model.IEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unuldur on 07/01/2018.
 */

public class AlarmManagerEvent {
    private static AlarmManagerEvent event;
    public static AlarmManagerEvent getInstance(Context context){
        if(event == null){
            event = new AlarmManagerEvent(context);
        }
        return event;
    }

    private AlarmManager alarmMgr;
    private Context context;

    public AlarmManagerEvent(Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    public void createNotifications(List<IEvent> events, long timeLimit){
        cancelAlarm(events);
        for (IEvent event : events) {
            if(event.getStartDate().getTimeInMillis() < System.currentTimeMillis()) continue;
            Intent intent = new Intent(context, AlarmReceiverEvent.class);
            intent.putExtra("event", event);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int)event.getStartDate().getTimeInMillis(), intent, 0);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, event.getStartDate().getTimeInMillis() - timeLimit, alarmIntent);
        }
    }

    public void cancelAlarm(List<IEvent> events){
        for (IEvent event : events)
        {
            if (alarmMgr!= null) {
                Intent intent = new Intent(context, AlarmReceiverEvent.class);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int)event.getStartDate().getTimeInMillis(), intent, 0);
                alarmMgr.cancel(alarmIntent);
            }
        }
    }
}
