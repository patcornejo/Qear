package com.patcornejo.qear.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by patcornejo on 14-09-15.
 */
public class App extends Application {

    private static App instance;
    private static Context context;

    public static App getInstance() {
        return instance;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
    }
}
