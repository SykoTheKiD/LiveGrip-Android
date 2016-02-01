package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.EventListAdapter;
import com.jaysyko.wrestlechat.dataObjects.Event;
import com.jaysyko.wrestlechat.listeners.RecyclerItemClickListener;
import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.models.Query;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.models.intentKeys.IntentKeys;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ERROR_LOADING_EVENTS = "Error loading events";
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateEventCards();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setDrawerHeaderUsername(String username) {
        View drawerNavHeader = getLayoutInflater().inflate(R.layout.nav_header_event_list, null);
        TextView userNameTV = (TextView) drawerNavHeader.findViewById(R.id.drawer_username);
        userNameTV.setText(username);
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
        // Handle navigation vie.w item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_my_profile) {

        } else if (id == R.id.nav_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //display clickable a list of all users
    private void updateEventCards() {
        events = new ArrayList<>();
        Query<ParseObject> query = new Query<>(Events.class);
        query.getQuery().findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> eventList, com.parse.ParseException e) {
                ParseObject current;
                if (e == null) {
                    for (int i = 0; i < eventList.size(); i++) {
                        current = eventList.get(i);
                        events.add(
                                new Event(
                                        current.get(Events.EVENT_NAME).toString(),
                                        current.get(Events.EVENT_LOCATION).toString(),
                                        current.get(Events.EVENT_START_TIME).toString(),
                                        current.get(Events.EVENT_END_TIME).toString(),
                                        current.get(Events.EVENT_IMAGE_ID).toString()
                                )
                        );
                    }
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(
                                    EventListActivity.this,
                                    recyclerView,
                                    new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            openConversation(eventList.get(position));
                                        }

                                        @Override
                                        public void onItemLongClick(View view, int position) {
                                            Vibrator vibe = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                            vibe.vibrate(50);
                                            openEventInfo(eventList.get(position));
                                        }
                                    }));
                    EventListAdapter mAdapter = new EventListAdapter(events, getApplicationContext());
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                } else {
                    Toast.makeText(getApplicationContext(),
                            ERROR_LOADING_EVENTS,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openConversation(ParseObject event) {
        Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
        intent.putExtra(IntentKeys.EVENT_ID, event.getObjectId());
        intent.putExtra(IntentKeys.EVENT_NAME, event.get(Events.EVENT_NAME).toString());
        startActivity(intent);
    }

    private void openEventInfo(ParseObject event) {
        Intent intent = new Intent(getApplicationContext(), EventInfoActivity.class);
        intent.putExtra(IntentKeys.EVENT_NAME, event.get(Events.EVENT_NAME).toString());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        setDrawerHeaderUsername(ParseUser.getCurrentUser().get(User.USERNAME_KEY).toString());
        super.onResume();
    }
}
