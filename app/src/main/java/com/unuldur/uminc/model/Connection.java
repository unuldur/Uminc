package com.unuldur.uminc.model;

import android.content.Context;
import android.provider.DocumentsContract;
import android.util.Log;

import com.unuldur.uminc.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * Created by Unuldur on 06/12/2017.
 */

public class Connection implements IConnection{
    private static final String LOG_TAG = "Connection";
    private Context context;

    public Connection(Context context) {
        this.context = context;
    }

    @Override
    public IEtudiant connect(String numEtu, String mdp) {
        final String studentId = numEtu;
        final String studentPassword = mdp;
        Log.d(LOG_TAG, "Starting sync");

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw response as a string.
        String dbufrHtmlStr = null;

        try {
            final String DBUFR_BASE_URL =
                    "https://www-dbufr.ufr-info-p6.jussieu.fr/lmd/2004/master/auths/seeStudentMarks.php";
            //   "http://192.168.0.1:8000";
            URL url = new URL(DBUFR_BASE_URL);

            // Set the authenticator with student id and password
            Authenticator.setDefault(new Authenticator() {
                boolean alreadyTriedAuthenticating = false;

                protected PasswordAuthentication getPasswordAuthentication() {
                    if (!alreadyTriedAuthenticating) {
                        alreadyTriedAuthenticating = true;
                        return new PasswordAuthentication(studentId, studentPassword.toCharArray());
                    } else {
                        return null;
                    }
                }
            });

            Log.d(LOG_TAG, "Trying to connect");
            // Create the request to dbufr, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(3000);
            urlConnection.connect();
            Log.d(LOG_TAG, "Connect");
            int responseCode = urlConnection.getResponseCode();

            Log.d(LOG_TAG, "Response code : " + responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d(LOG_TAG, "Error in connection");
                return null;
            }

            Log.v(LOG_TAG, "Connected");

            // Read the input stream into a String
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

            dbufrHtmlStr = buffer.toString();
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
        Log.d(LOG_TAG,dbufrHtmlStr);
        return new Etudiant();
    }
}
