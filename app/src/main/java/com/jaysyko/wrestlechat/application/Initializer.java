package com.jaysyko.wrestlechat.application;

import android.app.Application;

import com.jaysyko.wrestlechat.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

public class Initializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this);
    }
}
