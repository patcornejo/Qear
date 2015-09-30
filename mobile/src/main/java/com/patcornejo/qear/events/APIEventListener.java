package com.patcornejo.qear.events;

/**
 * Created by patcornejo on 14-09-15.
 */
public class APIEventListener implements APIInteface {
    @Override
    public void onData(String result) {}
}

interface APIInteface {
    void onData(String result);
}
