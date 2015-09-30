package com.patcornejo.qear.events;

/**
 * Created by patcornejo on 14-09-15.
 */
public class GoogleListener implements GoogleInterface {

    @Override
    public void onConnected() {}
}

interface GoogleInterface {
    void onConnected();
}
