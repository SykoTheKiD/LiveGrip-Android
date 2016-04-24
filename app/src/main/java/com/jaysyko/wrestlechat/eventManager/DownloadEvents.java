package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.CustomNetworkResponse;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkRequest;
import com.jaysyko.wrestlechat.network.NetworkSingleton;
import com.jaysyko.wrestlechat.network.RESTEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarushaan on 2016-04-23
 * TODO: Add 5 seconds wait for update
 */
public class DownloadEvents {
    private static DownloadEvents ourInstance = new DownloadEvents();
    private static final String TAG = DownloadEvents.class.getSimpleName();
    private List<Event> mEventsList = new ArrayList<>();
    private Context mApplicationContext;
    private Request mStringRequest = new NetworkRequest(new NetworkCallback() {
        @Override
        public void onSuccess(String response) {
            try {
                CustomNetworkResponse customNetworkResponse = new CustomNetworkResponse(response);
                if (customNetworkResponse.isSuccessful()) {
                    JSONObject current;
                    JSONArray events = customNetworkResponse.getPayload();
                    mEventsList.clear();
                    for (int index = 0; index < events.length(); index++) {
                        current = (JSONObject) events.get(index);
                        Event event = new Event(
                                current.getString(Event.EventJSONKeys.ID.toString()),
                                current.getString(Event.EventJSONKeys.NAME.toString()),
                                current.getString(Event.EventJSONKeys.INFO.toString()),
                                current.getString(Event.EventJSONKeys.MATCH_CARD.toString()),
                                current.getString(Event.EventJSONKeys.IMAGE.toString()),
                                current.getString(Event.EventJSONKeys.LOCATION.toString()),
                                current.getString(Event.EventJSONKeys.START_TIME.toString()),
                                current.getString(Event.EventJSONKeys.END_TIME.toString())
                        );
                        mEventsList.add(event);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }).get(RESTEndpoints.EVENTS);

    public static DownloadEvents getInstance(Context context) {
        ourInstance.mApplicationContext = context;
        return ourInstance;
    }

    public static DownloadEvents getInstance(Context context) {
        ourInstance.mApplicationContext = context;
        return ourInstance;
    }

    private DownloadEvents() {
    }

    public List<Event> getEventList() {
        return mEventsList;
    }

    public synchronized void updateEvents() {
        NetworkSingleton.getInstance(mApplicationContext).addToRequestQueue(mStringRequest);
    }
}
