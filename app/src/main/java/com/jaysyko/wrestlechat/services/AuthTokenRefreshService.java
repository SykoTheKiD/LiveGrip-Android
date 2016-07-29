package com.jaysyko.wrestlechat.services;

import android.app.IntentService;
import android.content.Intent;

import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.models.Token;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.APIInterface;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.requestData.AuthData;
import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;
import com.jaysyko.wrestlechat.network.responses.UserResponse;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class AuthTokenRefreshService extends IntentService {
    private static final String TAG = AuthTokenRefreshService.class.getSimpleName();
    private static final int ONE_WEEK_BEFORE = -1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public AuthTokenRefreshService() {
        super(AuthTokenRefreshService.class.getSimpleName());
    }


    private void refreshToken() {
        final User currentUser = SessionManager.getCurrentUser();
        if(currentUser != null){
            final Token token = currentUser.getToken();
            String tokenExpiryDate = token.getExpiryDate();
            Date expiryDate = Timestamp.valueOf(tokenExpiryDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expiryDate);
            calendar.add(Calendar.WEEK_OF_MONTH, ONE_WEEK_BEFORE);
            Date weekBefore = calendar.getTime();
            Date dateNow = new Date();
            if (dateNow.after(weekBefore)) {
                APIInterface apiManager = ApiManager.getApiService();
                Call<UserResponse> call = apiManager.getUser(
                        new AuthData(currentUser.getUsername(), currentUser.getPassword())
                );
                ApiManager.request(call, new NetworkCallback<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse response) {
                        eLog.e(TAG, "Token Refreshed");
                        currentUser.setAuthToken(response.getData().getToken());
                    }

                    @Override
                    public void onFail(FailedRequestResponse error) {
                        eLog.e(TAG, "Token Refresh Failed");
                        eLog.e(TAG, error.getMessage());
                    }
                });
            }
        }else{
            eLog.e(TAG, "null user");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        refreshToken();
    }
}
