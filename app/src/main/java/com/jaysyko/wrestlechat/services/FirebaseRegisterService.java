package com.jaysyko.wrestlechat.services;

import android.content.SharedPreferences;
import android.os.Handler;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceKeys;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;

public class FirebaseRegisterService extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseRegisterService.class.getSimpleName();
    private final Handler handler = new Handler();

    @Override
    public void onTokenRefresh(){
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        eLog.i(TAG, refreshedToken);
        handler.post(new Runnable() {
            @Override
            public void run() {
                final SharedPreferences.Editor editor = PreferenceProvider.getEditor(getApplicationContext(), Preferences.FCM);
                editor.putString(PreferenceKeys.FCM_KEY, refreshedToken);
                editor.putBoolean(PreferenceKeys.FCM_UPDATED, false);
                editor.apply();
            }
        });
    }
}
