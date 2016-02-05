package com.jaysyko.wrestlechat.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import com.jaysyko.wrestlechat.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;


public class ParseInitializer extends Application {
    Handler networkVerifier = new Handler();
    Context context = getApplicationContext();

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this);

    }
}
