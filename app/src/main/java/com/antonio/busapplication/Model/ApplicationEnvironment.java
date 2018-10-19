package com.antonio.busapplication.Model;

import android.app.Application;
import android.content.Context;

/**
 * Created by Antonio on 5/9/2017.
 */

public class ApplicationEnvironment extends Application {

    private static ApplicationEnvironment instance;

    public static ApplicationEnvironment getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
