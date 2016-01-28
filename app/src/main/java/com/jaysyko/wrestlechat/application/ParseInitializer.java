package com.jaysyko.wrestlechat.application;

import android.app.Application;

import com.jaysyko.wrestlechat.models.db.ParseMessageModel;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class ParseInitializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ParseMessageModel.class);
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
