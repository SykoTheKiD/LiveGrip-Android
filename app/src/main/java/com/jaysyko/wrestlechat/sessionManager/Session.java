package com.jaysyko.wrestlechat.sessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.jaysyko.wrestlechat.auth.CurrentActiveUser;

/**
 * @author Jay Syko
 */
public class Session {

    private static SharedPreferences sharedPreferences;

    public static void newSession(Context context) {
        sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("auth_token", CurrentActiveUser.getInstance().getCurrentUser().getAuthToken());
        editor.apply();
    }

    public static void destroySession(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
