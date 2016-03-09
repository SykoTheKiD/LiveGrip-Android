package com.jaysyko.wrestlechat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.adapters.MessageListAdapter;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formValidators.MessageValidator;
import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.query.Query;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.jaysyko.wrestlechat.db.BackEnd.queryDB;

public class MessagingFragment extends Fragment {

    public static final int FETCH_MSG_DELAY_MILLIS = 1000, MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private static final int SEND_DELAY = 1500;
    private String userName, sEventId;
    private EditText etMessage;
    private ListView lvChat;
    private ArrayList<Message> messages;
    private MessageListAdapter mAdapter;
    private ImageButton btSend;
    private Context applicationContext;
    private View view;
    // Keep track of initial load to scroll to the bottom of the ListView
    private boolean mFirstLoad = true;
    private Handler handler = new Handler();
    private Runnable fetchNewMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            fetchNewMessages();
            handler.postDelayed(this, FETCH_MSG_DELAY_MILLIS);
        }
    };
    private Runnable initMessageAdapter = new Runnable() {
        @Override
        public void run() {
            initMessageAdapter();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messaging, container, false);
        sEventId = CurrentActiveEvent.getInstance().getEventID();
        CurrentActiveUser currentUser = CurrentActiveUser.getInstance();
        userName = currentUser.getUsername();
        btSend = (ImageButton) view.findViewById(R.id.btSend);
        applicationContext = getContext();
        handler.post(initMessageAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSend.setEnabled(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String body = etMessage.getText().toString();
                        Form form = new MessageValidator(body).validate();
                        if (NetworkState.isConnected(applicationContext)) {
                            if (form.isValid()) {
                                fetchNewMessages();
                                body = body.trim();
                                // Use Message model to create new messages now
                                Message message = new Message();
                                message.setUsername(userName);
                                message.setEventId(sEventId);
                                message.setBody(body);
                                message.setUserImage(CurrentActiveUser.getInstance().getCustomProfileImageURL());
                                message.saveInBackground();
                                etMessage.setText(StringResources.NULL_TEXT);
                            } else {
                                Dialog.makeToast(applicationContext, getString(Form.getSimpleMessage(form.getReason())));
                            }
                        } else {
                            Dialog.makeToast(applicationContext, getString(R.string.no_network));
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btSend.setEnabled(true);
                            }
                        }, SEND_DELAY);
                    }
                });
            }
        });
        return view;
    }

    // Setup message field and posting
    private void initMessageAdapter() {
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        lvChat = (ListView) view.findViewById(R.id.lvChat);
        messages = new ArrayList<>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mAdapter = new MessageListAdapter(applicationContext, userName, messages);
        lvChat.setAdapter(mAdapter);
        mFirstLoad = true;
    }

    // Query messages from Parse so we can load them into the chat adapter
    @SuppressWarnings("unchecked")
    private synchronized void fetchNewMessages() {
        if (NetworkState.isConnected(applicationContext)) {
            Query query = new Query(Message.class);
            query.whereEqualTo(Events.ID, sEventId);
            query.orderByDESC(Message.CREATED_AT);
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            List messages = queryDB(query, Message.class.getSimpleName());
            if (messages != null) {
                Collections.reverse(messages);
                this.messages.clear();
                this.messages.addAll(messages);
                mAdapter.notifyDataSetChanged(); // update adapter
                // Scroll to the bottom of the list on initial load
                if (mFirstLoad) {
                    mFirstLoad = false;
                    lvChat.setSelection(mAdapter.getCount() - 1);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(fetchNewMessagesRunnable);
    }
}
