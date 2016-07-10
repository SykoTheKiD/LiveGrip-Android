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
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.MessageListAdapter;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.eventManager.CurrentActiveEvent;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formTypes.MessagingForm;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.network.responses.MessageGetResponse;
import com.jaysyko.wrestlechat.services.IMessageArrivedListener;
import com.jaysyko.wrestlechat.services.MessagingService;
import com.jaysyko.wrestlechat.services.MessagingServiceBinder;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.ArrayList;

import retrofit2.Call;

public class MessagingFragment extends Fragment implements IMessageArrivedListener {

    private static final String TAG = MessagingFragment.class.getSimpleName();
    private static final int SEND_DELAY = 1500;
    private static final String FONT_COLOR_HTML = "<font color=\"#FFFFFFF\">", FONT_HTML = "</font>";
    private static ArrayList<Message> mMessages = new ArrayList<>();
    private static MessageListAdapter mAdapter;
    private EditText etMessage;
    private Button btSend;
    private Context mApplicationContext;
    private View view;
    private Handler handler = new Handler();
    private boolean mServiceBound = false;
    private Intent mChatServiceIntent;
    private MessagingService messagingService;
    private Runnable initMessageAdapter = new Runnable() {
        @Override
        public void run() {
            initMessageAdapter();
        }
    };
    private Event mCurrentEvent = CurrentActiveEvent.getInstance().getCurrentEvent();
    private MessagingServiceBinder mMessagingServiceBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessagingServiceBinder = (MessagingServiceBinder) service;
            mMessagingServiceBinder.setMessageArrivedListener(MessagingFragment.this);
            messagingService = mMessagingServiceBinder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessagingServiceBinder.getService().disconnect();
            mServiceBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.setTitle(Html.fromHtml(FONT_COLOR_HTML + mCurrentEvent.getEventName() + FONT_HTML));
        mChatServiceIntent = new Intent(activity, MessagingService.class);
        if (!mServiceBound) {
            getActivity().startService(mChatServiceIntent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messaging, container, false);
        mApplicationContext = getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) mApplicationContext).setSupportActionBar(toolbar);
        btSend = (Button) view.findViewById(R.id.send_button);
        handler.post(initMessageAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });
        return view;
    }

    private void onSend() {
        btSend.setEnabled(false);
        handler.post(new Runnable() {
            @Override
            public void run() {
                saveMessage(etMessage.getText().toString().trim());
            }
        });
    }

    private void saveMessage(String body) {
        Form form = new MessagingForm(body).validate();
        if (NetworkState.isConnected(mApplicationContext)) {
            if (form.isValid()) {
                messagingService.send(body);
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
        etMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onSend();
                }
                return false;
            }
        });
        ListView lvChat = (ListView) view.findViewById(R.id.chat_list_view);
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        mAdapter = new MessageListAdapter(mApplicationContext, mMessages);
        lvChat.setAdapter(mAdapter);
    }

    @Override
    public void messageArrived(Message message) {
        mAdapter.add(message);
    }

    @Override
    public void onStart() {
        super.onStart();
        mApplicationContext.bindService(mChatServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mAdapter.getCount() == 0){
            getChatHistory();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceBound) {
            getActivity().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    private void getChatHistory() {
        if(NetworkState.isConnected(mApplicationContext)){
            Call<MessageGetResponse> getMessagesCall = ApiManager.getApiService().getMessages(
                    SessionManager.getCurrentUser().getAuthToken(),
                    CurrentActiveEvent.getInstance().getCurrentEvent().getEventID()
            );
            ApiManager.request(getMessagesCall, new NetworkCallback<MessageGetResponse>() {
                @Override
                public void onSuccess(MessageGetResponse response) {
                    mAdapter.addAll(response.getData());
                }

                @Override
                public void onFail(String error) {
                    eLog.e(TAG, error);
                }
            });
        }else{
            Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
        }
    }
}
