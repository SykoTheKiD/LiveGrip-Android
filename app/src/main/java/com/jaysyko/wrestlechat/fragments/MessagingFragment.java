package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.adapters.MessageListAdapter;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formValidators.MessageValidator;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.models.MessageJSONKeys;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkRequest;
import com.jaysyko.wrestlechat.network.NetworkResponse;
import com.jaysyko.wrestlechat.network.NetworkSingleton;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.RESTEndpoints;
import com.jaysyko.wrestlechat.services.ChatStream;
import com.jaysyko.wrestlechat.services.ChatStreamBinder;
import com.jaysyko.wrestlechat.utils.StringResources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagingFragment extends Fragment {

    public static final String TAG = MessagingFragment.class.getSimpleName();
    private static final int SEND_DELAY = 1500;
    private static ArrayList<Message> mMessages = new ArrayList<>();
    private static MessageListAdapter mAdapter;
    private EditText etMessage;
    private ImageButton btSend;
    private Context mApplicationContext;
    private View view;
    private Handler handler = new Handler();
    private boolean mServiceBound = false;
    private Intent mChatServiceIntent;
    private Runnable initMessageAdapter = new Runnable() {
        @Override
        public void run() {
            initMessageAdapter();
        }
    };
    private Event mCurrentEvent = CurrentActiveEvent.getInstance().getCurrentEvent();
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent mChatServiceIntent) {
//            updateMessages((Message) mChatServiceIntent.getSerializableExtra(IntentKeys.MESSAGE_BROADCAST));
//        }
//    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChatStreamBinder binder = (ChatStreamBinder) service;
            ChatStream chatStream = binder.getService();
            chatStream.subscribe(CurrentActiveEvent.getInstance().getCurrentEvent().getEventID());
//            mApplicationContext.registerReceiver(broadcastReceiver, new IntentFilter(ChatStream.TAG));
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            mApplicationContext.unregisterReceiver(broadcastReceiver);
            mServiceBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.setTitle(mCurrentEvent.getEventName());
        mChatServiceIntent = new Intent(activity, ChatStream.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messaging, container, false);
        mApplicationContext = getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) mApplicationContext).setSupportActionBar(toolbar);
        btSend = (ImageButton) view.findViewById(R.id.send_button);
        handler.post(initMessageAdapter);
        handler.post(new Runnable() {
            @Override
            public void run() {
                fetchOldMessages();
            }
        });
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
                // Use Message model to create new mMessages now
//                Message message = new Message();
//                message.setUserID(userID);
//                message.setEventId(sEventId);
//                message.setBody(body);
//                ChatStream.getInstance().send(message);
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
        mAdapter = new MessageListAdapter(mApplicationContext, mMessages);
        lvChat.setAdapter(mAdapter);
    }

    private void stopMessagingService() {
        if (mServiceBound) {
            getActivity().stopService(mChatServiceIntent);
            getActivity().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    private void updateMessages(Message message) {
        mMessages.add(mMessages.size() - 1, message);
        mAdapter.notifyDataSetChanged();
    }

    private void fetchOldMessages() {
        if (NetworkState.isConnected(mApplicationContext)) {
            HashMap<String, String> params = new HashMap<>();
            params.put(MessageJSONKeys.EVENT_ID.toString(), CurrentActiveEvent.getInstance().getCurrentEvent().getEventID());
            Request networkRequest = new NetworkRequest(new NetworkCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        NetworkResponse networkResponse = new NetworkResponse(response);
                        if (networkResponse.isSuccessful()) {
                            JSONObject current;
                            JSONArray messageObjects = networkResponse.getPayload();
                            for (int index = 0; index < messageObjects.length(); index++) {
                                current = (JSONObject) messageObjects.get(index);
                                mMessages.add(
                                        new Message(
                                                current.getString(MessageJSONKeys.USERNAME.toString()),
                                                current.getString(MessageJSONKeys.EVENT_NAME.toString()),
                                                current.getString(MessageJSONKeys.BODY.toString()),
                                                current.getString(MessageJSONKeys.PROFILE_IMAGE.toString())
                                        )
                                );
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }).post(RESTEndpoints.MESSAGES, params);
            NetworkSingleton.getInstance(mApplicationContext).addToRequestQueue(networkRequest);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mServiceBound) {
            mApplicationContext.startService(mChatServiceIntent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        getActivity().unregisterReceiver(broadcastReceiver);
        mApplicationContext.stopService(mChatServiceIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mApplicationContext.bindService(mChatServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceBound) {
            mApplicationContext.unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }
}
