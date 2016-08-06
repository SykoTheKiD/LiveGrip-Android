package com.jaysyko.wrestlechat.application;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

/**
 * Initializer.java
 * Initializes the app with the database
 *
 * @author Jay Syko
 */
public class Initializer extends Application {

    /**
     * Registers the app to the database
     */

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(App.debug).build()).build());
        Initializer.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return Initializer.context;
    }
}
