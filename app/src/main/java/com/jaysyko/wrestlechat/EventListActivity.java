package com.jaysyko.wrestlechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends Activity {

    public static final String ACTIVITY_TITLE = "Events";
    public static final String EVENT_LIST_KEY = "eventName";
    public static final String ERROR_LOADING_EVENTS = "Error loading events";
    public static final String INTENT_KEY_EVENT_ID = "EVENT_ID";
    public static final String INTENT_KEY_EVENT_NAME = "EVENT_NAME";
    private ArrayAdapter<String> namesArrayAdapter;
    private ArrayList<String> events;
    private ListView usersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        setTitle(ACTIVITY_TITLE);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //display clickable a list of all users
    private void setConversationsList() {
        events = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ACTIVITY_TITLE);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> eventList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < eventList.size(); i++) {
                        events.add(eventList.get(i).get(EVENT_LIST_KEY).toString());
                    }
                    usersListView = (ListView) findViewById(R.id.usersListView);
                    namesArrayAdapter =
                            new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.user_list_item, events);
                    usersListView.setAdapter(namesArrayAdapter);

                    usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                            openConversation(eventList.get(i));
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(),
                            ERROR_LOADING_EVENTS,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openConversation(ParseObject event) {
        Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
        intent.putExtra(INTENT_KEY_EVENT_ID, event.getObjectId());
        intent.putExtra(INTENT_KEY_EVENT_NAME, event.get(EVENT_LIST_KEY).toString());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        setConversationsList();
        super.onResume();
    }

}
