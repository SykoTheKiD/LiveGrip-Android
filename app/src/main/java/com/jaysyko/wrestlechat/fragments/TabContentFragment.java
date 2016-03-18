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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.EventListAdapter;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.eventManager.RetrieveEvents;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.BundleKeys;
import com.jaysyko.wrestlechat.utils.DBConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class TabContentFragment extends Fragment {

    private static final int VIBRATE_MILLISECONDS = 40;
    private static final int REFRESH_ANI_MILLIS = 3000;
    final Handler handler = new Handler();
    private List<Event> liveEvents = new ArrayList<>();
    private Context applicationContext;
    private EventListAdapter mAdapter;
    final Runnable updateEventsSoft = new Runnable() {
        @Override
        public void run() {
            refreshCards();
        }
    };
    final Runnable updateEventsHard = new Runnable() {
        @Override
        public void run() {
            if (NetworkState.isConnected(applicationContext)) {
                refreshCards();
            } else {
                Dialog.makeToast(applicationContext, getString(R.string.no_network));
            }
        }
    };
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
                mAdapter = new EventListAdapter(new ArrayList<Event>(), applicationContext);
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

    private void refreshCards() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                DBConstants.MYSQL_URL.concat("events.php"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean successful = jsonObject.getBoolean("success");
                            if (successful) {
                                JSONObject current;
                                JSONArray events = jsonObject.getJSONArray("payload");
                                for (int i = 0; i < events.length(); i++) {
                                    current = (JSONObject) events.get(i);
                                    String startTime = current.getString("start_time"), endTime = current.getString("end_time");
                                    if (DateVerifier.goLive(startTime, endTime).goLive()) {
                                        liveEvents.add(new Event(
                                                current.getString("id"),
                                                current.getString("name"),
                                                current.getString("info"),
                                                current.getString("match_card"),
                                                current.getString("image"),
                                                current.getString("location"),
                                                current.getString("start_time"),
                                                current.getString("end_time")
                                        ));
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERR", error.getMessage());
            }
        });
        RetrieveEvents.getInstance(applicationContext, stringRequest).updateEventCards();
        updateRecyclerView(liveEvents);
    }

    private synchronized void updateRecyclerView(List<Event> eventObjects) {
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
