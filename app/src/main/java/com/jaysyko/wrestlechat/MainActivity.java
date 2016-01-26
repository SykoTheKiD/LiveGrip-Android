package com.jaysyko.wrestlechat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this);
    }
}
