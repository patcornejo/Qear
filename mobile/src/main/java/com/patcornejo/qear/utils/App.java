package com.patcornejo.qear.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by patcornejo on 14-09-15.
 */
public class App extends Application {

    private static Application app;

    public static Context getContext() {
        return app.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
