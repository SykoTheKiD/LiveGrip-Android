package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.util.Log;

import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.responses.EventResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * CurrentEvents.java
 * <p/>
 * Singleton that holds and updates the current events
 *
 * @author Jay Syko
 */
public class CurrentEvents {
    private static final String TAG = CurrentEvents.class.getSimpleName();
    private static CurrentEvents ourInstance = new CurrentEvents();
    private List<Event> mEventsList = new ArrayList<>();
    private Context mApplicationContext;

    private CurrentEvents() {
    }

    public static CurrentEvents getInstance(Context context) {
        ourInstance.mApplicationContext = context;
        return ourInstance;
    }

    public List<Event> getEvents() {
        Call<EventResponse> call = ApiManager.getApiService().getEvents();
        ApiManager.request(call, new NetworkCallback<EventResponse>() {
            @Override
            public void onSuccess(EventResponse response) {
                mEventsList.clear();
                mEventsList.addAll(response.getEvents());
            }

            @Override
            public void onFail(String t) {
                Log.e(TAG, t);
                Dialog.makeToast(mApplicationContext, t);
            }
        });
        return mEventsList;
    }

    public synchronized void updateEvents() {
        //TODO implement get new events
    }
}
