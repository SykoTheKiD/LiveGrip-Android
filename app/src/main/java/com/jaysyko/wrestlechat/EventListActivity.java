package com.jaysyko.wrestlechat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
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

    private String currentUserId;
    private ArrayAdapter<String> namesArrayAdapter;
    private ArrayList<String> events;
    private ListView usersListView;
    private Button logoutButton;
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
    }

    //display clickable a list of all users
    private void setConversationsList() {
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        events = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
//        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> eventList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < eventList.size(); i++) {
                        events.add(eventList.get(i).get("eventName").toString());
                    }

                    usersListView = (ListView) findViewById(R.id.usersListView);
                    namesArrayAdapter =
                            new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.user_list_item, events);
                    usersListView.setAdapter(namesArrayAdapter);

                    usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                            Toast.makeText(getApplicationContext(),
                                    events.get(i),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error loading user list",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        setConversationsList();
        super.onResume();
    }

}
