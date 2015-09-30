package com.patcornejo.qear.services;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.patcornejo.qear.events.APIEventListener;
import com.patcornejo.qear.events.GoogleListener;
import com.patcornejo.qear.utils.API;
import com.patcornejo.qear.utils.Constants;
import com.patcornejo.qear.utils.Google;
import com.patcornejo.qear.utils.SendMessageThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageListener extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent msge) {
        Log.v("myTag", "onMessageReceived");
        final String message = new String(msge.getData());

        if (msge.getPath().equals(Constants.CONFIG_PATH)) {
            Log.v("myTag", "Message received on watch is: " + message);

            if(message.equals("activate")) {
                Google.getInstance().init(gl);

                if(!Google.getInstance().isConnected())
                    Google.getInstance().connect();
                else
                    new SendMessageThread(Constants.CONFIG_PATH, "connected").start();
            }

        } else if (msge.getPath().equals(Constants.QUERY_PATH)) {

            if(message.equals("getQuestion")) API.getQuestion(apil);
            else if(message.equals("getConfig")) API.getConfig(apil);

        } else {
            super.onMessageReceived(msge);
        }
    }

    private GoogleListener gl = new GoogleListener() {

        @Override
        public void onConnected() {
            Log.i("myTag", "onConnected");
            new SendMessageThread(Constants.CONFIG_PATH, "connected").start();
        }
    };

    private APIEventListener apil = new APIEventListener() {
        @Override
        public void onData(String result) {
            new SendMessageThread(Constants.QUERY_PATH, result).start();
        }
    };
}
