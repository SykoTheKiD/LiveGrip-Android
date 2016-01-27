package com.jaysyko.wrestlechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends Activity {

    public static final String EVENT_ID_INTENT_KEY = "EVENT_ID";
    public static final String EVENT_NAME_INTENT_KEY = "EVENT_NAME";
    public static final String NULL_TEXT = "";
    public static final String PARSE_EVENT_ID_KEY = "eventId";
    public static final String PARSE_CREATED_AT_KEY = "createdAt";
    public static final String LOG_KEY = "message";
    public static final String ERROR_KEY = "Error: ";
    public static final int FETCH_MSG_DELAY_MILLIS = 100;
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private static String sUserId;
    private static String sEventId;
    private EditText etMessage;
    private ListView lvChat;
    private ArrayList<ParseMessageModel> messages;
    private MessageListAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, FETCH_MSG_DELAY_MILLIS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Intent intent = getIntent();
        sEventId = intent.getStringExtra(EVENT_ID_INTENT_KEY);
        setTitle(intent.getStringExtra(EVENT_NAME_INTENT_KEY));
        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            Intent redirectToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(redirectToLogin);
        }
        handler.postDelayed(runnable, FETCH_MSG_DELAY_MILLIS);
    }

    // Get the userId from the cached currentUser object
    private void startWithCurrentUser() {
        sUserId = ParseUser.getCurrentUser().getObjectId();
        setupMessagePosting();
    }

    private void refreshMessages() {
        receiveMessage();
    }

    // Setup message field and posting
    private void setupMessagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        Button btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        messages = new ArrayList<ParseMessageModel>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new MessageListAdapter(MessagingActivity.this, sUserId, messages);
        lvChat.setAdapter(mAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                if (body.isEmpty()) {
                    return;
                } else {
                    body = body.trim();
                    // Use ParseMessageModel model to create new messages now
                    ParseMessageModel parseMessageModel = new ParseMessageModel();
                    parseMessageModel.setUserId(sUserId);
                    parseMessageModel.setEventId(sEventId);
                    parseMessageModel.setBody(body);
                    parseMessageModel.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            receiveMessage();
                        }
                    });
                    etMessage.setText(NULL_TEXT);
                }
            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<ParseMessageModel> query = ParseQuery.getQuery(ParseMessageModel.class);
        // Configure limit and sort order
        query.whereEqualTo(PARSE_EVENT_ID_KEY, sEventId);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending(PARSE_CREATED_AT_KEY);
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<ParseMessageModel>() {
            public void done(List<ParseMessageModel> parseMessageModels, ParseException e) {
                if (e == null) {
                    messages.clear();
                    messages.addAll(parseMessageModels);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.d(LOG_KEY, ERROR_KEY + e.getMessage());
                }
            }
        });
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
//    private void login() {
//        ParseAnonymousUtils.logIn(new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e != null) {
//                    Log.d(TAG, "Anonymous login failed: " + e.toString());
//                } else {
//                    startWithCurrentUser();
//                }
//            }
//        });
//    }
}
