package com.jaysyko.wrestlechat.auth;

import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.models.Token;
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
 * @author Jay Syko on 2016-07-23.
 */
public class TokenUtils {
    private static final String TAG = TokenUtils.class.getSimpleName();

    public static void refreshToken(){
        Token token = SessionManager.getCurrentUser().getToken();
        String tokenExpiryDate = token.getExpiryDate();
        Date expiryDate = Timestamp.valueOf(tokenExpiryDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expiryDate);
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        Date weekBefore = calendar.getTime();
        Date dateNow = new Date();
        APIInterface apiManager = ApiManager.getApiService();
        Call<UserResponse> call = apiManager.getUser(
                new AuthData(SessionManager.getCurrentUser().getUsername(), "")
        );
        if(dateNow.after(weekBefore)){
            ApiManager.request(call, new NetworkCallback<UserResponse>() {
                @Override
                public void onSuccess(UserResponse response) {

                }

                @Override
                public void onFail(FailedRequestResponse error) {
                    eLog.e(TAG, error.getMessage());

                }
            });
        }
    }
}
