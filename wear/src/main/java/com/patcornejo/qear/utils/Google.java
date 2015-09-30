package com.patcornejo.qear.utils;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.patcornejo.qear.events.GoogleListener;

/**
 * Created by patcornejo on 14-09-15.
 */
public class Google {

    private static GoogleApiClient gac;
    private static GoogleListener gl;
    private static Google g;

    public Google() {
        gac = new GoogleApiClient.Builder(App.getInstance().getContext())
                .addApi(Wearable.API)
                .addConnectionCallbacks(connl)
                .addOnConnectionFailedListener(failedl)
                .build();
    }

    public static Google getInstance() {

        if(gac == null) g = new Google();

        return g;
    }

    public void init(GoogleListener gl) {
        Google.gl = gl;
        connect();
    }

    public void connect() {
        if(gac != null)
            if(!gac.isConnected() && !gac.isConnecting())
                gac.connect();
    }

    public void disconnect() {
        if(gac != null)
            if(gac.isConnected())
                gac.disconnect();
    }

    public GoogleApiClient getGac() {
        return gac;
    }

    public Boolean isConnected() {
        return gac.isConnected();
    }

    private static GoogleApiClient.ConnectionCallbacks connl = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) { gl.onConnected(); }

        @Override
        public void onConnectionSuspended(int i) {}
    };

    private static GoogleApiClient.OnConnectionFailedListener failedl = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {}
    };
}
