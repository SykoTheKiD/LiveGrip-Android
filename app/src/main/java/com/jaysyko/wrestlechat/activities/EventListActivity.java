package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.EventListAdapter;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.conversation.CurrentActiveEvent;
import com.jaysyko.wrestlechat.dataObjects.EventObject;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.date.LiveStatus;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.query.Query;
import com.jaysyko.wrestlechat.utils.StringResources;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int VIBRATE_MILLISECONDS = 40;
    private static final int REFRESH_ANI_MILLIS = 2500;
    final Handler handler = new Handler();
    private Context applicationContext;
    private List<ParseObject> eventList;
    private EventListAdapter mAdapter;
    final Runnable updateEventsSoft = new Runnable() {
        @Override
        public void run() {
            updateEventCards(false);
        }
    };
    final Runnable updateEventsHard = new Runnable() {
        @Override
        public void run() {
            updateEventCards(true);
        }
    };
    final Runnable initSwipeRefresh = new Runnable() {
        @Override
        public void run() {
            initSwipeRefresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("METHOD", "onCreate()");
        applicationContext = getApplicationContext();

        handler.post(initSwipeRefresh);
        handler.post(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(applicationContext));
                eventListClickListener(recyclerView);
                mAdapter = new EventListAdapter(new ArrayList<EventObject>(), applicationContext);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_event_list);
        TextView headerUsername = (TextView) headerLayout.findViewById(R.id.drawer_username);
        headerUsername.setText(CurrentActiveUser.getInstance().getUsername());

    }

    private void initSwipeRefresh() {
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case (R.id.nav_my_profile):
                Dialog.makeDialog(EventListActivity.this, getString(R.string.upcoming),
                        getString(R.string.profile_upcoming));
                break;
            case (R.id.nav_logout):
                if (NetworkState.isConnected(applicationContext)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            CurrentActiveUser.getInstance().logout();
                            Intent intent = new Intent(applicationContext, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Dialog.makeToast(applicationContext, getString(R.string.no_network));
                }
                break;
            case (R.id.nav_share):
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(StringResources.PLAIN_CONTENT_TYPE);
                share.putExtra(Intent.EXTRA_TEXT, R.string.app_share);
                startActivity(Intent.createChooser(share, getString(R.string.app_share_title)));
                break;
            case (R.id.nav_about):
                Intent aboutIntent = new Intent(applicationContext, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case (R.id.nav_legal):
                Intent legalIntent = new Intent(applicationContext, LegalActivity.class);
                startActivity(legalIntent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //display clickable a list of all users
    @SuppressWarnings("unchecked")
    private void updateEventCards(Boolean hard) {
        ArrayList<EventObject> eventObjects = new ArrayList<>();
        if (NetworkState.isConnected(applicationContext)) {
            Query query = new Query(Events.class);
            query.orderByASC(Events.START_TIME);
            if (hard) {
                eventList = query.executeHard();
            } else {
                eventList = query.execute();
            }
            ParseObject current;
            if (eventList != null) {
                if (eventList.size() > 0) {
                    for (int i = 0; i < eventList.size(); i++) {
                        current = eventList.get(i);
                        eventObjects.add(
                                new EventObject(
                                        current.getString(Events.NAME),
                                        current.getString(Events.LOCATION),
                                        current.getLong(Events.START_TIME),
                                        current.getLong(Events.END_TIME),
                                        current.getString(Events.IMAGE)
                                )
                        );
                    }
                } else {
                    Dialog.makeToast(applicationContext, getString(R.string.no_events));
                }
            } else {
                Dialog.makeToast(applicationContext, getString(R.string.error_loading_events));
            }
        } else {
            Dialog.makeToast(applicationContext, getString(R.string.no_network));
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
                        EventListActivity.this, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                openConversation(eventList.get(position));
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                Vibrator vibe = (Vibrator) applicationContext.getSystemService(Context.VIBRATOR_SERVICE);
                                vibe.vibrate(VIBRATE_MILLISECONDS);
                                openEventInfo(eventList.get(position));
                            }
                        }));
    }

    private void openConversation(ParseObject event) {
        LiveStatus status = DateVerifier.goLive(event.getLong(Events.START_TIME), event.getLong(Events.END_TIME));
        if (status.goLive()) {
            CurrentActiveEvent.getInstance().setCurrentEvent(event);
            Intent intent = new Intent(applicationContext, MessagingActivity.class);
            startActivity(intent);
        } else {
            Dialog.makeToast(applicationContext, getString(status.getReason()));
        }
    }

    private void openEventInfo(ParseObject event) {
        CurrentActiveEvent.getInstance().setCurrentEvent(event);
        Intent intent = new Intent(applicationContext, EventInfoActivity.class);
        startActivity(intent);
    }

    public void onStart(){
        super.onStart();
        handler.post(updateEventsHard);
    }

    public void onResume(){
        super.onResume();
        handler.post(updateEventsSoft);
    }

}