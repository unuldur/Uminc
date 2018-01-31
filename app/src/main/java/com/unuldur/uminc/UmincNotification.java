package com.unuldur.uminc;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Unuldur on 31/01/2018.
 */

public class UmincNotification {
    private static int MID = 0;

    public static void launchNotification(String text, Context context){
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context, Notification.CATEGORY_EVENT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Uminc")
                .setContentText(text)
                .setSound(alarmSound);
        notificationManager.notify(MID, mNotifyBuilder.build());
        MID++;
    }
}
