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
    private static int MID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        IEvent event = (IEvent)intent.getSerializableExtra("event");
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context, Notification.CATEGORY_EVENT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Uminc")
                .setContentText(event.getTitre() + " - " + event.getLocalisation())
                .setSound(alarmSound);
        notificationManager.notify(MID, mNotifyBuilder.build());
        MID++;
    }
}
