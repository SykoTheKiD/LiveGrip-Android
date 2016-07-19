package com.jaysyko.wrestlechat.application;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
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
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }
}
