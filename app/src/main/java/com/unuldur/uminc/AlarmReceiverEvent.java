package com.unuldur.uminc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.unuldur.uminc.model.IEvent;

/**
 * Created by Unuldur on 07/01/2018.
 */

public class AlarmReceiverEvent extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        IEvent event = (IEvent)intent.getSerializableExtra("event");
        Log.d("Alarm", event + "  " + context + " " + intent);
        Log.d("Alarm", "yay " + intent.getSerializableExtra("event"));
        UmincNotification.launchNotification(event.getTitre() + " - " + event.getLocalisation(), context);
    }
}
