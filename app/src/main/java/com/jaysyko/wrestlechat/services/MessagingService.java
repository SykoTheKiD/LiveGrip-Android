package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.fragments.MessagingFragment;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.models.MessageJSONKeys;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkRequest;
import com.jaysyko.wrestlechat.network.NetworkResponse;
import com.jaysyko.wrestlechat.network.NetworkSingleton;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.RESTEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jarushaan on 2016-03-09
 */
public class MessagingService extends Service {
    private static final String TAG = MessagingService.class.getSimpleName();
    private final IBinder mBinder = new MessageBinder(this);
    private Handler handler = new Handler();
    private List<Message> messageList = new ArrayList<>();
    private final Runnable fetchMessageRunnable = new Runnable() {
        @Override
        public void run() {
            fetchOldMessages();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler.post(fetchMessageRunnable);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    private void fetchOldMessages() {
        if (NetworkState.isConnected(getApplicationContext())) {
            HashMap<String, String> params = new HashMap<>();
            params.put(MessageJSONKeys.EVENT_NAME.getKey(), CurrentActiveEvent.getInstance().getCurrentEvent().getEventID());
            Request networkRequest = new NetworkRequest(new NetworkCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        NetworkResponse networkResponse = new NetworkResponse(response);
                        if (networkResponse.isSuccessful()) {
                            JSONObject current;
                            JSONArray messages = networkResponse.getPayload();
                            for (int i = 0; i < messages.length(); i++) {
                                current = (JSONObject) messages.get(i);
                                messageList.add(
                                        new Message(
                                                current.getString(MessageJSONKeys.USERNAME.getKey()),
                                                current.getString(MessageJSONKeys.EVENT_NAME.getKey()),
                                                current.getString(MessageJSONKeys.BODY.getKey()),
                                                current.getString(MessageJSONKeys.PROFILE_IMAGE.getKey())
                                        )
                                );
                            }
                            update(messageList);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }).post(RESTEndpoints.MESSAGES, params);
            NetworkSingleton.getInstance(getApplicationContext()).addToRequestQueue(networkRequest);
        }
    }

    public void update(List<Message> messages) {
        MessagingFragment.updateMessages(messages);
    }
}
