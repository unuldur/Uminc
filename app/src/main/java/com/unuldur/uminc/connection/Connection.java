package com.unuldur.uminc.connection;

import android.content.Context;
import android.util.Log;

import com.unuldur.uminc.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by julie on 11/12/2017.
 */

public class Connection implements IConnection {

    private Context context;
    private static final String LOG_TAG = "Connection";

    public Connection(Context context) {
        this.context = context;
    }

    @Override
    public String getPage(String address) {
        return getPage(address, null, null);
    }

    @Override
    public String getPage(final String address, final String username, final String password) {
        return getPage(address,"GET", username, password, 0);
    }

    @Override
    public String getPage(String address, String type, final String username,final String password, int depth) {
        Log.d(LOG_TAG, "Starting sync");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        Log.d(LOG_TAG, address);
        String pageStr ;

        try {
            URL url = new URL(address);

            if(username != null) {
                Authenticator.setDefault(new Authenticator() {
                    boolean alreadyTriedAuthenticating = false;

                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (!alreadyTriedAuthenticating) {
                            alreadyTriedAuthenticating = true;
                            return new PasswordAuthentication(username, password.toCharArray());
                        } else {
                            return null;
                        }
                    }
                });
            }

            Log.d(LOG_TAG, "Trying to connect");
            urlConnection = (HttpURLConnection) url.openConnection();
            try {
                urlConnection.setRequestMethod(type);
            } catch (final ProtocolException pe) {
                urlConnection.setRequestProperty("X-HTTP-Method-Override", type);
                urlConnection.setRequestMethod("POST");
            }
            urlConnection.setRequestProperty("depth", ""+depth);
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(3000);
            urlConnection.connect();
            Log.d(LOG_TAG, "Connect");
            int responseCode = urlConnection.getResponseCode();

            Log.d(LOG_TAG, "Response code : " + responseCode);

            Log.v(LOG_TAG, "Connected");

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader =
                    new BufferedReader(
                            new InputStreamReader(inputStream, context.getString(R.string.dbufr_charset)));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            pageStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream ", e);
                }
            }
        }
        return pageStr;
    }
}
