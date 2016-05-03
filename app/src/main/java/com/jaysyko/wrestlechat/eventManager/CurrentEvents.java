package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.jaysyko.wrestlechat.localStorage.LocalStorage;
import com.jaysyko.wrestlechat.localStorage.StorageFile;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.CustomNetworkResponse;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkCallbackObject;
import com.jaysyko.wrestlechat.network.NetworkRequest;
import com.jaysyko.wrestlechat.network.NetworkSingleton;
import com.jaysyko.wrestlechat.network.RESTEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * CurrentEvents.java
 *
 * Singleton that holds and updates the current events
 * @author Jay Syko
 */
public class CurrentEvents {
    private static final String TAG = CurrentEvents.class.getSimpleName();
    private static final String EVENTS = "events";
    private static CurrentEvents ourInstance = new CurrentEvents();
    private List<Event> mEventsList = new ArrayList<>();
    private Context mApplicationContext;
    private static SharedPreferences mSharedPreferences;

    private CurrentEvents() {
    }

    public static CurrentEvents getInstance(Context context) {
        ourInstance.mApplicationContext = context;
        mSharedPreferences = new LocalStorage(context, StorageFile.EVENTS).getSharedPreferences();
        return ourInstance;
    }

    private void jsonToModel(JSONArray events) {
        JSONObject current;
        mEventsList.clear();
        for (int index = 0; index < events.length(); index++) {
            try {
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
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void saveToCache(String eventsJSON) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(EVENTS, eventsJSON);
        editor.apply();
    }

    public List<Event> getEvents() {
        String events = mSharedPreferences.getString(EVENTS, null);
        if(events != null){
            try {
                JSONArray eventsJSON = new JSONArray(events);
                jsonToModel(eventsJSON);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return mEventsList;
    }

    public synchronized void updateEvents(final NetworkCallbackObject<Event> callback) {
        Request stringRequest = new NetworkRequest(new NetworkCallback() {
            @Override
            public void onSuccess(String response) {
                CustomNetworkResponse customNetworkResponse = new CustomNetworkResponse(response);
                if (customNetworkResponse.isSuccessful()) {
                    JSONArray events = customNetworkResponse.getPayloadArray();
                    saveToCache(events.toString());
                    jsonToModel(events);
                    callback.onSuccess(mEventsList);
                }
            }
        }).get(RESTEndpoints.EVENTS);
        NetworkSingleton.getInstance(mApplicationContext).addToRequestQueue(stringRequest);
    }
}
