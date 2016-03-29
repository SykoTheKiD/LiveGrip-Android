package com.jaysyko.wrestlechat.localStorage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jarushaan on 2016-03-25
 */
public class LocalStorage {
    private static final int MODE = 0;
    private SharedPreferences sharedPreferences;

    public LocalStorage(Context context, String preferenceName) {
        sharedPreferences = context.getSharedPreferences(preferenceName, MODE);
    }

    public SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

}
