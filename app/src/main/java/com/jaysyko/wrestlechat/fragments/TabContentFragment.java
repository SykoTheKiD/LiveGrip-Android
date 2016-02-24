package com.jaysyko.wrestlechat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
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
import com.jaysyko.wrestlechat.dataObjects.EventObject;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.eventManager.OpenEventConversation;
import com.jaysyko.wrestlechat.eventManager.RetrieveEvents;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.Keys;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class TabContentFragment extends Fragment {

    private static final int VIBRATE_MILLISECONDS = 40;
    private static final int REFRESH_ANI_MILLIS = 3000;
    final Handler handler = new Handler();
    private Context applicationContext;
    private EventListAdapter mAdapter;
    private RelativeLayout layout;
    private int state;
    private List<ParseObject> liveEvents = new ArrayList<>();
    final Runnable updateEventsSoft = new Runnable() {
        @Override
        public void run() {
            refreshCards(false);
        }
    };
    final Runnable updateEventsHard = new Runnable() {
        @Override
        public void run() {
            if (NetworkState.isConnected(applicationContext)) {
                refreshCards(true);
            } else {
                Dialog.makeToast(applicationContext, getString(R.string.no_network));
            }
        }
    };
    final Runnable initSwipeRefresh = new Runnable() {
        @Override
        public void run() {
            initSwipeRefresh();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.event_list_fragment_layout, null);
        applicationContext = getContext();
        this.state = getArguments().getInt(Keys.STATE_KEY);
        handler.post(initSwipeRefresh);
        handler.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
                eventListClickListener(recyclerView);
                mAdapter = new EventListAdapter(new ArrayList<EventObject>(), applicationContext);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });

        return layout;
    }

    private void initSwipeRefresh() {
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                if (NetworkState.isConnected(applicationContext)) {
                    (new Handler()).postDelayed(updateEventsHard, REFRESH_ANI_MILLIS);
                } else {
                    Dialog.makeToast(applicationContext, getString(R.string.no_network));
                }
                swipeView.setRefreshing(false);
            }
        });
    }

    private void refreshCards(Boolean hard) {
        ArrayList<EventObject> eventObjects = new ArrayList<>();
        ParseObject current;
        List<ParseObject> eventList = RetrieveEvents.getInstance().getEventList(hard);
        if (eventList != null) {
            if (eventList.size() > 0) {
                for (int i = 0; i < eventList.size(); i++) {
                    current = eventList.get(i);
                    Long startTime = current.getLong(Events.START_TIME), endTime = current.getLong(Events.END_TIME);
                    if (DateVerifier.goLive(startTime, endTime).getReason() == this.state) {
                        EventObject eventObject = new EventObject(
                                current.getString(Events.NAME),
                                current.getString(Events.LOCATION),
                                startTime,
                                endTime,
                                current.getString(Events.IMAGE)
                        );
                        eventObjects.add(eventObject);
                        liveEvents.add(current);
                    }
                }
            } else {
                Dialog.makeToast(applicationContext, getString(R.string.no_events));
            }
        } else {
            Dialog.makeToast(applicationContext, getString(R.string.error_loading_events));
        }
        updateRecyclerView(eventObjects);
    }

    private synchronized void updateRecyclerView(ArrayList<EventObject> eventObjects) {
        mAdapter.itemsData.clear();
        mAdapter.itemsData.addAll(eventObjects);
        mAdapter.notifyDataSetChanged();
    }

    private void eventListClickListener(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        applicationContext, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                OpenEventConversation.openConversation(liveEvents.get(position), applicationContext);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                Vibrator vibe = (Vibrator) applicationContext.getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(VIBRATE_MILLISECONDS);
                                OpenEventConversation.openEventInfo(liveEvents.get(position), applicationContext);
                            }
                        }));
    }

    public void onStart() {
        super.onStart();
        handler.post(updateEventsHard);
    }

    public void onResume() {
        super.onResume();
        handler.post(updateEventsSoft);
    }
}
