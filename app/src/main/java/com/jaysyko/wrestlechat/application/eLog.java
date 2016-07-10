package com.jaysyko.wrestlechat.application;

import android.util.Log;

/**
 * @author Jay Syko on 2016-07-09.
 */
public class eLog {

    private static final boolean LOGGING = App.debug;

    public static void i (String TAG, String msg){
        if (LOGGING){
            if (TAG != null){
                Log.w(TAG, "" + msg);
            }
        }
    }

    public static void e (String TAG, String msg){
        if (LOGGING){
            if (TAG != null){
                Log.e(TAG, "" + msg);
            }
        }
    }
}