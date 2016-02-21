package com.jaysyko.wrestlechat.application;

import android.app.Application;

import com.jaysyko.wrestlechat.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

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
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this);
    }
}
