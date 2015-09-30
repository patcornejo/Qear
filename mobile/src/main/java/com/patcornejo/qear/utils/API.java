package com.patcornejo.qear.utils;

import android.os.AsyncTask;

import com.patcornejo.qear.events.APIEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by patcornejo on 14-09-15.
 */
public class API {

    private static final String base = "http://ec2-54-174-105-52.compute-1.amazonaws.com";

    public static void getQuestion(final APIEventListener apil) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                return getQuery(base + "/questions/random");
            }

            @Override
            protected void onPostExecute(String result) {
                apil.onData(result);
            }
        }.execute();
    }

    public static void getConfig(final APIEventListener apil) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                return getQuery(base + "/configs/es");
            }

            @Override
            protected void onPostExecute(String result) {
                apil.onData(result);
            }
        }.execute();
    }

    protected static String getQuery(String strUrl) {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();

            if (statusCode >= 200 && statusCode < 400) iStream = urlConnection.getInputStream();
            else iStream = urlConnection.getErrorStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb  = new StringBuilder();

            String line;

            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch(Exception e){
            return "{'success': false, 'errorCode':'Error 1067 RESPONSE ERROR', 'errorMessage': '" + e.getMessage() + "'}";
        } finally{
            try {
                if(iStream != null)
                    iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return data;
    }
}
