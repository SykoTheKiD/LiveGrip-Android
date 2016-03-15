package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.services.MessageBinder;
import com.jaysyko.wrestlechat.services.MessagingService;
import com.jaysyko.wrestlechat.services.chatStream.ChatStream;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.ArrayList;
import java.util.List;

public class MessagingFragment extends Fragment {

    private static final int SEND_DELAY = 1500;
    private ArrayList<Message> messages = new ArrayList<>();
    private MessageListAdapter mAdapter;
    private String userID, sEventId;
    private EditText etMessage;
    private ImageButton btSend;
    private Activity mApplicationContext;
    private View view;
    private Handler handler = new Handler();
    private boolean mServiceBound = false;
    private Intent intent;
    private Runnable initMessageAdapter = new Runnable() {
        @Override
        public void run() {
            initMessageAdapter();
        }
    };
    private Event mCurrentEvent;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateMessages((Message) intent.getSerializableExtra("MSG"));
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessageBinder binder = (MessageBinder) service;
            MessagingService messagingService = binder.getService();
            updateMessages(messagingService.getMessageList());
            mApplicationContext.registerReceiver(broadcastReceiver, new IntentFilter(ChatStream.CLASS_NAME));
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mApplicationContext.unregisterReceiver(broadcastReceiver);
            mServiceBound = false;
        }
    };

    @SuppressWarnings("unchecked")
    private void updateMessages(List messageList) {
        this.messages.clear();
        this.messages.addAll(messageList);
        this.mAdapter.notifyDataSetChanged();
    }

    private void updateMessages(Message message) {
        ArrayList<Message> messages = this.messages;
        messages.add(messages.size() - 1, message);
        this.mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentEvent = CurrentActiveEvent.getInstance().getCurrentEvent();
        String eventName = mCurrentEvent.getEventName();
        mApplicationContext.setTitle(eventName);
        intent = new Intent(mApplicationContext, MessagingService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messaging, container, false);
        mApplicationContext = getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) mApplicationContext).setSupportActionBar(toolbar);
        sEventId = mCurrentEvent.getEventID();
        CurrentActiveUser currentUser = CurrentActiveUser.getInstance();
        userID = currentUser.getUserID();
        btSend = (ImageButton) view.findViewById(R.id.send_button);
        handler.post(initMessageAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSend.setEnabled(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        saveMessage(etMessage.getText().toString().trim());
                    }
                });
            }
        });
        return view;
    }

    private void saveMessage(String body) {
        Form form = new MessageValidator(body).validate();
        if (NetworkState.isConnected(mApplicationContext)) {
            if (form.isValid()) {
                // Use Message model to create new messages now
                Message message = new Message();
                message.setUserID(userID);
                message.setEventId(sEventId);
                message.setBody(body);
                ChatStream.getInstance().send(message);
                etMessage.setText(StringResources.NULL_TEXT);
            } else {
                Dialog.makeToast(mApplicationContext, getString(Form.getSimpleMessage(form.getReason())));
            }
        } else {
            Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btSend.setEnabled(true);
            }
        }, SEND_DELAY);
    }

    // Setup message field and posting
    private void initMessageAdapter() {
        etMessage = (EditText) view.findViewById(R.id.new_message_edit_text);
        ListView lvChat = (ListView) view.findViewById(R.id.chat_list_view);
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mAdapter = new MessageListAdapter(mApplicationContext, messages);
        lvChat.setAdapter(mAdapter);
    }

    private void stopMessagingService() {
        if (mServiceBound) {
            getActivity().stopService(intent);
            getActivity().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mServiceBound) {
            mApplicationContext.startService(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopMessagingService();
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), MessagingService.class);
        getActivity().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
