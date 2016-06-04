package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.util.Log;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkSingleton;
import com.jaysyko.wrestlechat.network.responses.EventResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * CurrentEvents.java
 *
 * Singleton that holds and updates the current events
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
        NetworkSingleton networkSingleton = NetworkSingleton.getInstance();
        Call<EventResponse> call = networkSingleton.getApiService().getEvents();
        networkSingleton.request(call, new NetworkCallback<EventResponse>() {
            @Override
            public void onSuccess(EventResponse response) {
                mEventsList.clear();
                mEventsList.addAll(response.getEvents());
            }

            @Override
            public void onFail(String t) {
                Log.e(TAG, t);
                Dialog.makeToast(mApplicationContext, mApplicationContext.getString(R.string.an_error_occured));
            }
        });
        return mEventsList;
    }

    public synchronized void updateEvents() {
        //TODO implement get new events
    }
}
