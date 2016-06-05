package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.responses.EventResponse;
import com.jaysyko.wrestlechat.sqlite.daos.EventDao;

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
public final class CurrentEvents {
    private static final String TAG = CurrentEvents.class.getSimpleName();
    private static CurrentEvents ourInstance = new CurrentEvents();
    private List<Event> mEventsList = new ArrayList<>();
    private Context mApplicationContext;
    private EventDao eventDao;
    private Handler handler = new Handler();

    private CurrentEvents() {
    }

    public static CurrentEvents getInstance(Context context) {
        ourInstance.mApplicationContext = context;
        ourInstance.eventDao = new EventDao(context);
        return ourInstance;
    }

    public List<Event> getEventsFromNetwork() {
        Call<EventResponse> call = ApiManager.getApiService().getEvents();
        ApiManager.request(call, new NetworkCallback<EventResponse>() {
            @Override
            public void onSuccess(EventResponse response) {
                mEventsList.clear();
                mEventsList.addAll(response.getEvents());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        eventDao.addAll(mEventsList);
                    }
                });
            }

            @Override
            public void onFail(String t) {
                Log.e(TAG, t);
                Dialog.makeToast(mApplicationContext, t);
            }
        });
        return mEventsList;
    }

    public List<Event> getEvents(){
        eventDao.open();
        mEventsList.addAll(eventDao.getAllEvents());
        eventDao.close();
        return mEventsList;

    }

}
