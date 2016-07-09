package com.jaysyko.wrestlechat.eventManager;

import android.util.Log;

import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.APIInterface;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.requestData.MessageData;
import com.jaysyko.wrestlechat.network.responses.MessageGetResponse;
import com.jaysyko.wrestlechat.network.responses.MessageSaveResponse;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * @author Jay Syko
 */
public class Messenger {

    private static final String TAG = Messenger.class.getSimpleName();
    private static final APIInterface apiManager = ApiManager.getApiService();
    private static final String USER_AUTH_TOKEN = SessionManager.getCurrentUser().getAuthToken();
    private static final Call<MessageGetResponse> getMessagesCall = apiManager.getMessages(
            USER_AUTH_TOKEN,
            CurrentActiveEvent.getInstance().getCurrentEvent().getEventID()
    );

    public static List<Message> getMessages() {
        final List<Message> ret = new ArrayList<>();
        ApiManager.request(getMessagesCall, new NetworkCallback<MessageGetResponse>() {
            @Override
            public void onSuccess(MessageGetResponse response) {
                ret.addAll(response.getData());
            }

            @Override
            public void onFail(String error) {
                Log.e(TAG, "" + error);
            }
        });
        return ret;
    }

    public static void saveMessage(final String body) {

        final User currentUser = SessionManager.getCurrentUser();
        final Call<MessageSaveResponse> saveMessagesCall = apiManager.saveMessage(
                USER_AUTH_TOKEN,
                new MessageData(
                        CurrentActiveEvent.getInstance().getCurrentEvent().getEventID(),
                        currentUser.getId(),
                        body,
                        currentUser.getUsername(),
                        currentUser.getProfileImage()
                )
        );

        ApiManager.request(saveMessagesCall, new NetworkCallback<MessageSaveResponse>() {
            @Override
            public void onSuccess(MessageSaveResponse response) {
                Log.i(TAG, "Message Saved");
            }

            @Override
            public void onFail(String error) {
                Log.e(TAG, "" + error);
                Log.e(TAG, "Message was not saved to DB");
            }
        });
    }
}
