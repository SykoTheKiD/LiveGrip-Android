package com.jaysyko.wrestlechat.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;

import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

public class Initializer extends Application {

    private static View view;
    Handler internetHandler = new Handler();
    Runnable internetChecker = new Runnable() {
        @Override
        public void run() {
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (!(nf != null && nf.isConnected())) {
                Dialog.makeSnackBar(view);
            }
            internetHandler.postDelayed(this, 1000);
        }
    };

    public static void setView(View view) {
        Initializer.view = view;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(this);
        internetHandler.postDelayed(internetChecker, 1000);
    }
}
