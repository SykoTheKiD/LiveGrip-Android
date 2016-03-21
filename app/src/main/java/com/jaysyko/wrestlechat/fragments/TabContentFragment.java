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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.EventListAdapter;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.eventManager.OpenEvent;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.models.EventJSONKeys;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkRequest;
import com.jaysyko.wrestlechat.network.NetworkResponse;
import com.jaysyko.wrestlechat.network.NetworkSingleton;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.RESTEndpoints;
import com.jaysyko.wrestlechat.utils.BundleKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class TabContentFragment extends Fragment {
    private static final String TAG = TabContentFragment.class.getSimpleName();
    private static final int VIBRATE_MILLISECONDS = 40;
    final Handler handler = new Handler();
    private Context mApplicationContext;
    private EventListAdapter mAdapter;
    private RelativeLayout layout;
    private int state;
    private List<Event> mEventsList = new ArrayList<>();
    private Request mStringRequest = new NetworkRequest(new NetworkCallback() {
        @Override
        public void onSuccess(String response) {
            try {
                NetworkResponse networkResponse = new NetworkResponse(response);
                if (networkResponse.isSuccessful()) {
                    JSONObject current;
                    JSONArray events = networkResponse.getPayload();
                    mEventsList.clear();
                    for (int index = 0; index < events.length(); index++) {
                        current = (JSONObject) events.get(index);
                        String start_time = current.getString(EventJSONKeys.START_TIME.toString());
                        String end_time = current.getString(EventJSONKeys.END_TIME.toString());
                        if (DateVerifier.goLive(start_time, end_time).getReason() == state) {
                            Event event = new Event(
                                    current.getString(EventJSONKeys.ID.toString()),
                                    current.getString(EventJSONKeys.NAME.toString()),
                                    current.getString(EventJSONKeys.INFO.toString()),
                                    current.getString(EventJSONKeys.MATCH_CARD.toString()),
                                    current.getString(EventJSONKeys.IMAGE.toString()),
                                    current.getString(EventJSONKeys.LOCATION.toString()),
                                    start_time,
                                    end_time
                            );
                            mEventsList.add(event);
                        }
                    }
                    updateRecyclerView(mEventsList);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }).post(RESTEndpoints.EVENTS);
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
        mApplicationContext = getContext();
        this.state = getArguments().getInt(BundleKeys.STATE_KEY);
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
                NetworkSingleton.getInstance(mApplicationContext).addToRequestQueue(mStringRequest);
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
                if (NetworkState.isConnected(mApplicationContext)) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            NetworkSingleton.getInstance(mApplicationContext).addToRequestQueue(mStringRequest);
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



    private synchronized void updateRecyclerView(List<Event> eventObjects) {
        mAdapter.itemsData.clear();
        mAdapter.itemsData.addAll(eventObjects);
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
}
