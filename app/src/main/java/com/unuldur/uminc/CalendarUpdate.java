package com.unuldur.uminc;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.unuldur.uminc.calendarRecuperator.ICalendarRecuperator;
import com.unuldur.uminc.calendarRecuperator.MyCalendarDownloader;
import com.unuldur.uminc.calendarRecuperator.SimpleCalendarRecuperator;
import com.unuldur.uminc.connection.Connection;
import com.unuldur.uminc.model.Etudiant;
import com.unuldur.uminc.model.ICalendar;
import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.IEvent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

/**
 * Created by Unuldur on 28/01/2018.
 */

public class CalendarUpdate {

    public void update(Context context) {
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
        if(etu == null) return;

        UmincNotification.launchNotification("Mise à jour calendrier...", context);

        SyncroniseCalendar sync = new SyncroniseCalendar(context, etu);
        sync.execute();
    }



    private class SyncroniseCalendar  extends AsyncTask<Void, Void, ICalendar> {
        private final ICalendarRecuperator connection;
        private final IEtudiant etudiant;
        private final Context context;
        SyncroniseCalendar(Context context, IEtudiant etudiant) {
            this.etudiant = etudiant;
            this.context = context;

            connection = new SimpleCalendarRecuperator(new MyCalendarDownloader(new Connection(context),context));
        }

        @Override
        protected ICalendar doInBackground(Void... params) {
            return connection.getCalendar(etudiant);
        }

        @Override
        protected void onPostExecute(final ICalendar success) {

            if (success != null) {
                etudiant.addCalendar(success);
                String filename = context.getString(R.string.etudiant_saver);
                FileOutputStream outputStream;
                try {
                    outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    oos.writeObject(etudiant);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                if(sharedPref.getBoolean(context.getString(R.string.notif_file), true)) {
                    AlarmManagerEvent.getInstance(context).createNotifications(etudiant.getCalendar().getAllEvents(), 15 * 60 * 1000);
                }
            }
        }
    }
}
