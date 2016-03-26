package com.jaysyko.wrestlechat.auth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jarushaan on 2016-03-25
 */
public class SessionManager {
    private static final int MODE = 0;
    private static final String PREF_NAME = "AUTH";
    private static SessionManager sessionManager;
    private SharedPreferences sharedPreferences;

    private SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SessionManager getSessionManager(Context context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

}
