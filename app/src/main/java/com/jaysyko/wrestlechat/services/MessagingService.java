package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.db.BackEnd;
import com.jaysyko.wrestlechat.fragments.MessagingFragment;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.DBConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    DBConstants.MYSQL_URL.concat("messages.php"),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean successful = jsonObject.getBoolean("success");
                                if (successful) {
                                    JSONObject current;
                                    JSONArray messages = jsonObject.getJSONArray("payload");
                                    for (int i = 0; i < messages.length(); i++) {
                                        current = (JSONObject) messages.get(i);
                                        messageList.add(
                                                new Message(
                                                        current.getString("username"),
                                                        current.getString("name"),
                                                        current.getString("body"),
                                                        current.getString("profile_image")
                                                )
                                        );
                                    }
                                    update(messageList);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("event_id", CurrentActiveEvent.getInstance().getCurrentEvent().getEventID());
                    return params;
                }
            };
            new BackEnd(getApplicationContext()).execute(stringRequest);
        }
    }

    public void update(List<Message> messages) {
        MessagingFragment.updateMessages(messages);
    }
}
