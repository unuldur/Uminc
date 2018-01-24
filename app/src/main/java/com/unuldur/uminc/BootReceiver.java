package com.unuldur.uminc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
        }
    }
}
