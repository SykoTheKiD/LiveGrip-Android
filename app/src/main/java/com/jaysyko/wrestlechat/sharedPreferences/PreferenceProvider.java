package com.jaysyko.wrestlechat.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Jay Syko on 2016-07-12.
 */
public class PreferenceProvider {

    public static SharedPreferences.Editor getEditor(Context context, Preferences preference){
        return getSharedPreferences(context, preference).edit();
    }

    public static SharedPreferences getSharedPreferences(Context context, Preferences preference){
        if(preference == Preferences.SETTINGS){
            return PreferenceManager.getDefaultSharedPreferences(context);
        }

        final SharedPreferences sharedPreferences = context.getSharedPreferences(preference.toString(), Context.MODE_PRIVATE);
        if (preference == Preferences.SESSION){
            return new EncryptedSharedPreferences(context, sharedPreferences);
        }
        return sharedPreferences;
    }
}
