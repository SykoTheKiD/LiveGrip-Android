package com.jaysyko.wrestlechat.eventManager;

import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.APIInterface;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.requestData.MessageData;
import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;
import com.jaysyko.wrestlechat.network.responses.GenericResponse;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;

import retrofit2.Call;

/**
 * @author Jay Syko
 */
public class Messenger {

    private static final String TAG = Messenger.class.getSimpleName();
    private static final APIInterface apiManager = ApiManager.getApiService();
    private static final String USER_AUTH_TOKEN = SessionManager.getCurrentUser().getAuthToken();

    public static void saveMessage(final String body) {

        final User currentUser = SessionManager.getCurrentUser();
        final Call<GenericResponse> saveMessagesCall = apiManager.saveMessage(
                USER_AUTH_TOKEN,
                new MessageData(
                        CurrentActiveEvent.getInstance().getCurrentEvent().getEventID(),
                        currentUser.getId(),
                        body,
                        currentUser.getUsername(),
                        currentUser.getProfileImage()
                )
        );

        ApiManager.request(saveMessagesCall, new NetworkCallback<GenericResponse>() {
            @Override
            public void onSuccess(GenericResponse response) {
                eLog.i(TAG, "Message Saved");
            }

            @Override
            public void onFail(FailedRequestResponse error) {
                eLog.e(TAG, error.getMessage());
                eLog.e(TAG, "Message was not saved to DB");
            }
        });
    }
}
