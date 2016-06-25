package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.responses.EventResponse;
import com.jaysyko.wrestlechat.sessionManager.Session;
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
        Call<EventResponse> call = ApiManager.getApiService().getEvents(Session.getInstance().getCurrentUser().getAuthToken());
        ApiManager.request(call, new NetworkCallback<EventResponse>() {
            @Override
            public void onSuccess(EventResponse response) {
                mEventsList.clear();
                mEventsList.addAll(response.getData());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        eventDao.open();
                        eventDao.refresh();
                        eventDao.addAll(mEventsList);
                    }
                });
            }

            @Override
            public void onFail(String error) {
                Log.e(TAG, error);
                Dialog.makeToast(mApplicationContext, error);
            }
        });
        return mEventsList;
    }

    public List<Event> getEvents(){
        eventDao.open();
        mEventsList.clear();
        mEventsList.addAll(eventDao.getAllEvents());
        return mEventsList;
    }

}
