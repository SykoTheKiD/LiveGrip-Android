package com.jaysyko.wrestlechat.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.requestData.UpdateFCMData;
import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;
import com.jaysyko.wrestlechat.network.responses.GenericResponse;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceKeys;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;

import retrofit2.Call;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class FCMRegistrationService extends IntentService {

    private static final String TAG = FCMRegistrationService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FCMRegistrationService() {
        super(FCMRegistrationService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerToken();
    }

    private void registerToken() {
        final Context applicationContext = getApplicationContext();
        final SharedPreferences sharedPreferences = PreferenceProvider.getSharedPreferences(applicationContext, Preferences.FCM);
        final String currentToken = FirebaseInstanceId.getInstance().getToken();
        final boolean fcmUpdated = sharedPreferences.getBoolean(PreferenceKeys.FCM_UPDATED, false);
        if (!fcmUpdated) {
            eLog.i(TAG + "Firebase Token", currentToken);
            final User user = SessionManager.getCurrentUser();
            if (user != null) {
                Call<GenericResponse> call = ApiManager.getApiService().updateFCMID(
                        user.getAuthToken(), new UpdateFCMData(user.getId(), currentToken));

                ApiManager.request(call, new NetworkCallback<GenericResponse>() {
                    @Override
                    public void onSuccess(GenericResponse response) {
                        eLog.i(TAG, response.getStatus());
                        final SharedPreferences.Editor editor = PreferenceProvider.getEditor(applicationContext, Preferences.FCM);
                        editor.putBoolean(PreferenceKeys.FCM_UPDATED, true);
                        editor.apply();
                    }

                    @Override
                    public void onFail(FailedRequestResponse error) {
                        eLog.e(TAG, error.getMessage());
                    }
                });
            } else {
                eLog.e(TAG, "User is null");
            }
        }
    }
}
