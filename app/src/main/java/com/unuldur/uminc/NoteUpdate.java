package com.unuldur.uminc;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.unuldur.uminc.model.DbufrConnection;
import com.unuldur.uminc.model.IEtudiant;
import com.unuldur.uminc.model.Note;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unuldur on 26/01/2018.
 */

public class NoteUpdate {
    private static int MID = 0;

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

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context, Notification.CATEGORY_EVENT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Uminc")
                .setContentText("Mise à jour notes...")
                .setSound(alarmSound);
        notificationManager.notify(MID, mNotifyBuilder.build());
        MID++;

        SyncroniseNotesIn sync = new SyncroniseNotesIn(etu, context);
        sync.execute();
    }

    public class SyncroniseNotesIn extends SyncroniseNotes {
        private IEtudiant etudiant;
        private Context context;
        public SyncroniseNotesIn(IEtudiant etudiant, Context context) {
            super(etudiant, context);
            this.etudiant = etudiant;
            this.context = context;
        }


        @Override
        protected void onPostExecute(List<Note> notes) {

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if(notes != null){
                for(Note n : notes){
                    if(!etudiant.getNotes().contains(n)){
                        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context, Notification.CATEGORY_STATUS)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Uminc")
                                .setContentText("Nouvelle note : " + n.toString())
                                .setSound(alarmSound);
                        notificationManager.notify(MID, mNotifyBuilder.build());
                        MID++;
                    }
                }
                etudiant.setNotes(notes);
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
            }
        }
    }
}
