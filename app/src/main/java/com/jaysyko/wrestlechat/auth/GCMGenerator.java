package com.jaysyko.wrestlechat.auth;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.jaysyko.wrestlechat.R;

/**
 * Created by jarushaan on 2016-03-28
 */
public class GCMGenerator {
    private static final String TAG = GCMGenerator.class.getSimpleName();
    private Context context = null;

    public GCMGenerator(Context context) {
        this.context = context;
    }

    public String GCMRegister() throws Exception {
        String token = null;
        try {
            InstanceID gcmTokenInstanceID = InstanceID.getInstance(this.context);
            //we will get gcm_defaultSenderId by applying plugin: 'com.google.gms.google-services'
            token = gcmTokenInstanceID.getToken(this.context.getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (Exception e) {
            Log.i(TAG, "GCM Registration Token: " + e.getMessage());
        }
        //Keep this token securely, we use this to send message, refer in URL we have added this ID as parameter
        return token;
    }
}
