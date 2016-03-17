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
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.BundleKeys;

import java.util.ArrayList;

public class TabContentFragment extends Fragment {

    private static final int VIBRATE_MILLISECONDS = 40;
    private static final int REFRESH_ANI_MILLIS = 3000;
    final Handler handler = new Handler();
    //    private List<ParseObject> liveEvents = new ArrayList<>();
    final Runnable updateEventsSoft = new Runnable() {
        @Override
        public void run() {
            refreshCards(false);
        }
    };
    private Context applicationContext;
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
    private EventListAdapter mAdapter;
    private RelativeLayout layout;
    final Runnable initSwipeRefresh = new Runnable() {
        @Override
        public void run() {
            initSwipeRefresh();
        }
    };
    private int state;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.event_list_fragment_layout, null);
        applicationContext = getContext();
        this.state = getArguments().getInt(BundleKeys.STATE_KEY);
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
//        ArrayList<EventObject> eventObjects = new ArrayList<>();
//        ParseObject current;
//        List<ParseObject> eventList = RetrieveEvents.getInstance().getEventList(hard);
//        if (eventList != null) {
//            if (eventList.size() > 0) {
//                for (int i = 0; i < eventList.size(); i++) {
//                    current = eventList.get(i);
//                    Long startTime = current.getLong(DBConstants.EVENT_START_TIME_KEY), endTime = current.getLong(DBConstants.EVENT_END_TIME_KEY);
//                    if (DateVerifier.goLive(startTime, endTime).getReason() == this.state) {
//                        EventObject eventObject = new EventObject(
//                                current.getString(DBConstants.EVENT_NAME_KEY),
//                                current.getString(DBConstants.EVENT_LOCATION_KEY),
//                                startTime,
//                                endTime,
//                                current.getString(DBConstants.EVENT_IMAGE_KEY)
//                        );
//                        eventObjects.add(eventObject);
//                        liveEvents.add(current);
//                    }
//                }
//            } else {
//                Dialog.makeToast(applicationContext, getString(R.string.no_events));
//            }
//        } else {
//            Dialog.makeToast(applicationContext, getString(R.string.error_loading_events));
//        }
//        updateRecyclerView(eventObjects);
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
//                                OpenEvent.openConversation(liveEvents.get(position), applicationContext);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                Vibrator vibe = (Vibrator) applicationContext.getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(VIBRATE_MILLISECONDS);
//                                OpenEvent.openEventInfo(liveEvents.get(position), applicationContext);
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
