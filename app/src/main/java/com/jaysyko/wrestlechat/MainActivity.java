package com.jaysyko.wrestlechat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ParseMessageModel.class);
        Parse.initialize(this);
    }
}
