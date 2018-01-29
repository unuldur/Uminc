package com.unuldur.uminc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Unuldur on 29/01/2018.
 */

public class NetworkChangedReceiver extends BroadcastReceiver {
    private static Calendar date = Calendar.getInstance();
    private static boolean changedToday = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Calendar.getInstance().getTimeInMillis() - date.getTimeInMillis()> 1000*60){
            changedToday = false;
            date = Calendar.getInstance();
        }
        Log.d("Network",  " " + (date.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()));
        Log.d("Network", "changed " + changedToday);
        if(!changedToday && isOnline(context)) {
            CalendarUpdate cu = new CalendarUpdate();
            cu.update(context);
            NoteUpdate nu = new NoteUpdate();
            nu.update(context);
            changedToday = true;
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
