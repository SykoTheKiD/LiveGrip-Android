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
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.eventManager.CurrentEvents;
import com.jaysyko.wrestlechat.eventManager.OpenEvent;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.BundleKeys;

import java.util.ArrayList;
import java.util.List;

public class TabContentFragment extends Fragment {
    private static final int VIBRATE_MILLISECONDS = 40;
    private final Handler handler = new Handler();
    private Context mApplicationContext;
    private EventListAdapter mAdapter;
    private RelativeLayout layout;
    private final Runnable initSwipeRefresh = new Runnable() {
        @Override
        public void run() {
            initSwipeRefresh();
        }
    };
    private int state;
    private List<Event> mEventsList;
    private CurrentEvents currentEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_event_list, null);
        mApplicationContext = getContext();
        this.state = getArguments().getInt(BundleKeys.STATE_KEY);
        currentEvents = CurrentEvents.getInstance(mApplicationContext);
        mEventsList = currentEvents.getEvents();
        handler.post(initSwipeRefresh);
        handler.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(mApplicationContext));
                eventListClickListener(recyclerView);
                mAdapter = new EventListAdapter(new ArrayList<Event>(), mApplicationContext);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                currentEvents.getEvents();
            }
        });
        return layout;
    }

    @Override
    public void onStart(){
        super.onStart();
        currentEvents.getEvents();
    }

    private void initSwipeRefresh() {
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                if (NetworkState.isConnected(mApplicationContext)) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            generateEventCards(currentEvents.getEventsFromNetwork());
                            swipeView.setRefreshing(false);
                        }
                    });
                } else {
                    Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
                    swipeView.setRefreshing(false);
                }
            }
        });
    }

    private synchronized void updateRecyclerView() {
        mAdapter.itemsData.clear();
        mAdapter.itemsData.addAll(mEventsList);
        mAdapter.notifyDataSetChanged();
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

    private void generateEventCards(List<Event> response) {
        if(mAdapter != null){
            mEventsList.clear();
            for (Event event: response) {
                String start_time = event.getEventStartTime();
                String end_time = event.getEventEndTime();
                if (DateVerifier.goLive(start_time, end_time).getReason() == state) {
                    mEventsList.add(event);
                }
            }
            updateRecyclerView();
        }
    }
}
