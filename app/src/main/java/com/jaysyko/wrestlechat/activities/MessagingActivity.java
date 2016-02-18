package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.MessageListAdapter;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.conversation.CurrentActiveEvent;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.FormValidation;
import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.query.Query;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    public static final int FETCH_MSG_DELAY_MILLIS = 1000, MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private static final int SEND_DELAY = 1500;
    private String userName, sEventId, eventName;
    private EditText etMessage;
    private ListView lvChat;
    private ArrayList<Message> messages;
    private MessageListAdapter mAdapter;
    private Button btSend;
    private Context applicationContext;
    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad;
    private Handler handler = new Handler();
    private Runnable fetchNewMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            fetchNewMessages();
            handler.postDelayed(this, FETCH_MSG_DELAY_MILLIS);
        }
    };
    private boolean initMessages = handler.postDelayed(fetchNewMessagesRunnable, FETCH_MSG_DELAY_MILLIS);
    private Runnable initMessageaAdapter = new Runnable() {
        @Override
        public void run() {
            initMesssageAdapter();
        }
    };
    private boolean initAdapter = handler.post(initMessageaAdapter);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        sEventId = CurrentActiveEvent.getInstance().getEventID();
        eventName = CurrentActiveEvent.getInstance().getEventName();
        setTitle(eventName);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        CurrentActiveUser currentUser = CurrentActiveUser.getInstance();
        userName = currentUser.getUsername();
        btSend = (Button) findViewById(R.id.btSend);
        applicationContext = getApplicationContext();

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSend.setEnabled(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String body = etMessage.getText().toString();
                        Form form = FormValidation.validateMessage(body);
                        if (NetworkState.isConnected(applicationContext)) {
                            if (form.isValid()) {
                                body = body.trim();
                                // Use Message model to create new messages now
                                Message message = new Message();
                                message.setUsername(userName);
                                message.setEventId(sEventId);
                                message.setBody(body);
                                message.saveInBackground();
                                etMessage.setText(StringResources.NULL_TEXT);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        btSend.setEnabled(true);
                                    }
                                }, SEND_DELAY);
                            } else {
                                Dialog.makeToast(applicationContext, getString(Form.getSimpleMessage(form.getReason())));
                            }
                        } else {
                            Dialog.makeToast(applicationContext, getString(R.string.no_network));
                        }
                    }
                });
            }
        });
    }

    // Setup message field and posting
    private void initMesssageAdapter() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        lvChat = (ListView) findViewById(R.id.lvChat);
        messages = new ArrayList<>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mFirstLoad = true;
        mAdapter = new MessageListAdapter(MessagingActivity.this, userName, messages);
        lvChat.setAdapter(mAdapter);
    }

    // Query messages from Parse so we can load them into the chat adapter
    @SuppressWarnings("unchecked")
    private void fetchNewMessages() {
        if (NetworkState.isConnected(applicationContext)) {
            Query<Message> query = new Query<>(Message.class);
            query.whereEqualTo(Events.ID, sEventId);
            query.orderByDESC(Message.CREATED_AT);
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            List messages = query.executeHard();
            if (messages != null) {
                Collections.reverse(messages);
                MessagingActivity.this.messages.clear();
                MessagingActivity.this.messages.addAll(messages);
                mAdapter.notifyDataSetChanged(); // update adapter
                // Scroll to the bottom of the list on initial load
                if (mFirstLoad) {
                    lvChat.setSelection(mAdapter.getCount() - 1);
                    mFirstLoad = false;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.messaging_screen_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.action_event_info:
                Intent eventInfoIntent = new Intent(applicationContext, EventInfoActivity.class);
                startActivity(eventInfoIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        closeAllThreads();
    }

    public void onPause() {
        super.onPause();
        closeAllThreads();
    }

    private void closeAllThreads() {
        handler.removeCallbacks(fetchNewMessagesRunnable);
        handler.removeCallbacks(initMessageaAdapter);
    }
}