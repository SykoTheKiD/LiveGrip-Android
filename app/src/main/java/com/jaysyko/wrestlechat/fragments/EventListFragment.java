package com.jaysyko.wrestlechat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.EventListAdapter;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.eventManager.OpenEvent;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.responses.EventResponse;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;
import com.jaysyko.wrestlechat.sqlite.daos.EventDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class EventListFragment extends Fragment {

    private static final String TAG = EventListFragment.class.getSimpleName();
    private static final int VIBRATE_MILLISECONDS = 40;
    private final Handler handler = new Handler();
    private Context mApplicationContext;
    private EventListAdapter mAdapter;
    private RelativeLayout layout;
    private List<Event> mEventsList = new ArrayList<>();
    private EventDao eventDao;
    private SwipeRefreshLayout swipeView;
    final Runnable initSwipeRefresh = new Runnable() {
        @Override
        public void run() {
            initSwipeRefresh();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_event_list, null);
        mApplicationContext = getContext();
        getEvents();
        eventDao = new EventDao(mApplicationContext);
        handler.post(initSwipeRefresh);
        handler.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.events_recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
                eventListClickListener(recyclerView);
                mAdapter = new EventListAdapter(mEventsList, mApplicationContext);
                recyclerView.setAdapter(mAdapter);
            }
        });
        return layout;
    }

    /**
     * Initialize Pull to Refresh
     */
    private void initSwipeRefresh() {
        swipeView = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        getEvents();
                    }
                });
            }
        });
    }

    private synchronized void updateRecyclerView(List<Event> eventObjects) {
        if (mAdapter != null) {
            mAdapter.itemsData = eventObjects;
            mAdapter.notifyDataSetChanged();
        }
    }

    private void eventListClickListener(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        mApplicationContext, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                OpenEvent.openConversation(mEventsList.get(position), mApplicationContext);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                Vibrator vibe = (Vibrator) mApplicationContext.getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(VIBRATE_MILLISECONDS);
                                OpenEvent.openEventInfo(mEventsList.get(position), mApplicationContext);
                            }
                        }
                )
        );
    }

    private void getEvents() {
        if(NetworkState.isConnected(mApplicationContext)){
            Call<EventResponse> call = ApiManager.getApiService().getEvents(SessionManager.getCurrentUser().getAuthToken());
            ApiManager.request(call, new NetworkCallback<EventResponse>() {
                @Override
                public void onSuccess(EventResponse response) {
                    eLog.i(TAG, "Successfully received events from network");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            eventDao.open();
                            eventDao.refresh();
                            eventDao.addAll(mEventsList);
                        }
                    });
                    mEventsList.clear();
                    mEventsList.addAll(response.getData());
                }
                @Override
                public void onFail(String error) {
                    eLog.e(TAG, error);
                    Dialog.makeToast(mApplicationContext, error);
                }
            });
        }else{
            eventDao.open();
            mEventsList.clear();
            mEventsList.addAll(eventDao.getAllEvents());
            Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
        }
        swipeView.setRefreshing(false);
        updateRecyclerView(mEventsList);
    }
}