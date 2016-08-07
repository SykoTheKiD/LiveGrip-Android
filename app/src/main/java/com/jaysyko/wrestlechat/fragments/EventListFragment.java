package com.jaysyko.wrestlechat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.EventListAdapter;
import com.jaysyko.wrestlechat.ads.AdBuilder;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.eventManager.NotifyListStore;
import com.jaysyko.wrestlechat.eventManager.OpenEvent;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.responses.EventResponse;
import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;
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
    private AdBuilder adBuilder;
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
        adBuilder = new AdBuilder(getActivity());
        eventDao = new EventDao(mApplicationContext);
        handler.post(initSwipeRefresh);
        handler.post(new Runnable() {
            @Override
            public void run() {
                NotifyListStore.getInstance().restore(mApplicationContext);
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.events_recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
                eventListClickListener(recyclerView);
                mAdapter = new EventListAdapter(mEventsList, mApplicationContext);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                getEvents();
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                adBuilder.buildAd();
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
                getEvents();
            }
        });
    }

    private synchronized void updateRecyclerView(List<Event> eventObjects) {
        if (mAdapter != null) {
            mAdapter.itemsData = eventObjects;
            mAdapter.notifyDataSetChanged();
        }
    }

    private void eventListClickListener(final RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        mApplicationContext, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                OpenEvent.openConversation(mEventsList.get(position), getActivity());
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                Vibrator vibe = (Vibrator) mApplicationContext.getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(VIBRATE_MILLISECONDS);
                                OpenEvent.openEventInfo(mEventsList.get(position), getActivity());
                            }
                        }
                )
        );
    }

    private void getEvents() {
        if(swipeView != null){
            swipeView.setRefreshing(true);
        }
        if(NetworkState.isConnected()){
            Call<EventResponse> call = ApiManager.getApiService().getEvents(SessionManager.getCurrentUser().getAuthToken());
            ApiManager.request(call, new NetworkCallback<EventResponse>() {
                @Override
                public void onSuccess(final EventResponse response) {
                    eLog.i(TAG, "Successfully received events from network");
                    final List<Event> responseData = response.getData();
                    setEventsList(responseData);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            eventDao.open();
                            eventDao.refresh();
                            eventDao.addAll(responseData);
                        }
                    });
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final List<Event> cleanedEventsList = NotifyListStore
                                    .getInstance()
                                    .clean(responseData, mApplicationContext);
                            setEventsList(cleanedEventsList);
                        }
                    });
                    if (swipeView != null) {
                        swipeView.setRefreshing(false);
                    }
                }
                @Override
                public void onFail(FailedRequestResponse error) {
                    if (swipeView != null) {
                        swipeView.setRefreshing(false);
                    }
                }
            });
        }else{
            eLog.i(TAG, "Successfully received events from cache");
            eventDao.open();
            mEventsList.clear();
            mEventsList.addAll(eventDao.getAllEvents());
            Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
            if (swipeView != null) {
                swipeView.setRefreshing(false);
            }
        }
    }

    private void setEventsList(List<Event> events){
        mEventsList.clear();
        mEventsList.addAll(events);
        updateRecyclerView(mEventsList);
    }

    @Override
    public void onResume() {
        super.onResume();
        adBuilder.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        adBuilder.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotifyListStore.getInstance().storeList(mApplicationContext);
        adBuilder.onDestroy();
    }
}