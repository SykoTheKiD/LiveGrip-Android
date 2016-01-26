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

    public static final String USER_ID_KEY = "userId";
    private static final String TAG = MessagingActivity.class.getName();
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private static String sUserId;
    private static String sEventId;
    private EditText etMessage;
    private ListView lvChat;
    private ArrayList<ParseMessageModel> messages;
    private ChatListAdapter mAdapter;
    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            handler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        sEventId = intent.getStringExtra("EVENT_ID");
        // User login
        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            Intent redirectToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(redirectToLogin);
//            login();
        }
        handler.postDelayed(runnable, 100);
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
        mAdapter = new ChatListAdapter(MessagingActivity.this, sUserId, messages);
        lvChat.setAdapter(mAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
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
                etMessage.setText("");
            }
        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<ParseMessageModel> query = ParseQuery.getQuery(ParseMessageModel.class);
        // Configure limit and sort order
        query.whereEqualTo("eventId", sEventId);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
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
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void getEventHistory(){

    }

//    private void populateMessageHistory() {
//        String[] userIds = {currentUserId, recipientId};
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
//        query.whereContainedIn("senderId", Arrays.asList(userIds));
//        query.whereContainedIn("recipientId", Arrays.asList(userIds));
//        query.orderByAscending("createdAt");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> messageList, com.parse.ParseException e) {
//                if (e == null) {
//                    for (int i = 0; i < messageList.size(); i++) {
//                        WritableMessage message = new WritableMessage(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("messageText").toString());
//                        if (messageList.get(i).get("senderId").toString().equals(currentUserId)) {
//                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
//                        } else {
//                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
//                        }
//                    }
//                }
//            }
//        });
//    }

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
