package com.jaysyko.wrestlechat.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.requestData.UpdateFCMData;
import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;
import com.jaysyko.wrestlechat.network.responses.GenericResponse;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;

import retrofit2.Call;

public class FirebaseRegisterService extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseRegisterService.class.getSimpleName();

    @Override
    public void onTokenRefresh(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        eLog.i(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    public static void registerUserToken(){
        FirebaseRegisterService.sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
    }

    public static void sendRegistrationToServer(final String refreshedToken) {
        final User currentUser = SessionManager.getCurrentUser();
        Call<GenericResponse> call = ApiManager.getApiService().updateFCMID(
                currentUser.getAuthToken(),
                new UpdateFCMData(
                        currentUser.getId(),
                        refreshedToken
                )
        );

        ApiManager.request(call, new NetworkCallback<GenericResponse>() {
            @Override
            public void onSuccess(GenericResponse response) {
                eLog.i(TAG, response.getStatus());
            }

            @Override
            public void onFail(FailedRequestResponse error) {
                eLog.e(TAG, error.getMessage());
            }
        });
    }
}
