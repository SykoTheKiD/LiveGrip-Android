package com.jaysyko.wrestlechat;

import android.app.Application;

import com.parse.Parse;

public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        setContentView(R.layout.activity_main);
        Parse.initialize(this);
    }
}
