package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jaysyko.wrestlechat.localStorage.LocalStorage;
import com.jaysyko.wrestlechat.localStorage.StorageFile;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.APIInterface;
import com.jaysyko.wrestlechat.network.NetworkRequest;
import com.jaysyko.wrestlechat.network.responses.EventResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private static SharedPreferences mSharedPreferences;
    private List<Event> mEventsList = new ArrayList<>();
    private Context mApplicationContext;

    private CurrentEvents() {
    }

    public static CurrentEvents getInstance(Context context) {
        ourInstance.mApplicationContext = context;
        mSharedPreferences = new LocalStorage(context, StorageFile.EVENTS).getSharedPreferences();
        return ourInstance;
    }


    //    private void jsonToModel(JSONArray events) {
//        JSONObject current;
//        mEventsList.clear();
//        for (int index = 0; index < events.length(); index++) {
//            try {
//                current = (JSONObject) events.get(index);
//                Event event = new Event(
//                        current.getString(Event.EventJSONKeys.ID.toString()),
//                        current.getString(Event.EventJSONKeys.NAME.toString()),
//                        current.getString(Event.EventJSONKeys.INFO.toString()),
//                        current.getString(Event.EventJSONKeys.MATCH_CARD.toString()),
//                        current.getString(Event.EventJSONKeys.IMAGE.toString()),
//                        current.getString(Event.EventJSONKeys.LOCATION.toString()),
//                        current.getString(Event.EventJSONKeys.START_TIME.toString()),
//                        current.getString(Event.EventJSONKeys.END_TIME.toString())
//                );
//                mEventsList.add(event);
//            } catch (JSONException e) {
//                Log.e(TAG, e.getMessage());
//            }
//        }
//    }
//
//    private void saveToCache(String eventsJSON) {
//        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        editor.putString(EVENTS, eventsJSON);
//        editor.apply();
//    }
//
    public List<Event> getEvents() {
        APIInterface apiService =
                NetworkRequest.getClient().create(APIInterface.class);
        Call<EventResponse> call = apiService.getEvents();
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                Log.e(TAG, response.body().getStatus());
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
        return mEventsList;
    }
//
//    public synchronized void updateEvents(final NetworkCallbackObject<Event> callback) {
//        Request stringRequest = new NetworkRequest(new NetworkCallback() {
//            @Override
//            public void onSuccess(String response) {
//                CustomNetworkResponse customNetworkResponse = new CustomNetworkResponse(response);
//                if (customNetworkResponse.isSuccessful()) {
//                    JSONArray events = customNetworkResponse.getPayloadArray();
//                    saveToCache(events.toString());
//                    jsonToModel(events);
//                    callback.onSuccess(mEventsList);
//                }
//            }
//
//            @Override
//            public void onFail(String response) {
//                Dialog.makeToast(mApplicationContext, response);
//            }
//        }).get(RESTEndpoints.EVENTS);
//        NetworkSingleton.getInstance(mApplicationContext).addToRequestQueue(stringRequest);
//    }
}
