package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;
import com.jaysyko.wrestlechat.network.responses.MessageGetResponse;
import com.jaysyko.wrestlechat.services.IMessageArrivedListener;
import com.jaysyko.wrestlechat.services.MessagingService;
import com.jaysyko.wrestlechat.services.MessagingServiceBinder;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceKeys;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;
import com.jaysyko.wrestlechat.utils.StringResources;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import retrofit2.Call;

public class MessagingFragment extends Fragment implements IMessageArrivedListener, Observer {

    private static final String TAG = MessagingFragment.class.getSimpleName();
    private static final int SEND_DELAY = 1500;
    private static final String FONT_COLOR_HTML = "<font color=\"#FFFFFFF\">", FONT_HTML = "</font>";
    private View view;
    private boolean init;
    private Button btSend;
    private boolean mBound;
    private EditText etMessage;
    private Context mApplicationContext;
    private MessagingService mService;
    private int offset = 0;
    private final Handler handler = new Handler();
    private static MessageListAdapter mAdapter;
    private static ArrayList<Message> mMessages = new ArrayList<>();
    private Runnable initMessageAdapter = new Runnable() {
        @Override
        public void run() {
            initMessageAdapter();
        }
    };
    private Event mCurrentEvent = CurrentActiveEvent.getInstance().getCurrentEvent();
    private Runnable initSwipeAdapter = new Runnable() {
        @Override
        public void run() {
            initSwipeRefresh();
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MessagingServiceBinder mBinder = (MessagingServiceBinder) service;
            mService = mBinder.getService();
            mBinder.setMessageArrivedListener(MessagingFragment.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg) {
            mBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.setTitle(Html.fromHtml(FONT_COLOR_HTML + mCurrentEvent.getEventName() + FONT_HTML));
        NetworkState.getInstance().addObserver(this);
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
        handler.post(initSwipeAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });
        return view;
    }

    private void initSwipeRefresh() {
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                if (NetworkState.isConnected()) {
                    Call<MessageGetResponse> getMessagesCall = ApiManager.getApiService().getMessageHistory(
                            SessionManager.getCurrentUser().getAuthToken(),
                            CurrentActiveEvent.getInstance().getCurrentEvent().getEventID(),
                            offset
                    );
                    ApiManager.request(getMessagesCall, new NetworkCallback<MessageGetResponse>() {
                        @Override
                        public void onSuccess(MessageGetResponse response) {
                            eLog.i(TAG, "Fetched Chat History");
                            final List<Message> responseData = response.getData();
                            if(!responseData.isEmpty()){
                                Collections.reverse(responseData);
                                responseData.addAll(mMessages);
                                mAdapter.clear();
                                mAdapter.addAll(responseData);
                                offset++;
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onFail(FailedRequestResponse error) {
                            eLog.e(TAG, error.getMessage());
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = new Intent(mApplicationContext, MessagingService.class);
        mApplicationContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        SharedPreferences settings = PreferenceProvider.getSharedPreferences(mApplicationContext, Preferences.SETTINGS);
        Integer bg = Integer.parseInt(settings.getString(PreferenceKeys.SETTINGS_MESSAGING_WALLPAPER, PreferenceKeys.DEFAULT_SETTINGS_VALUE));
        eLog.i(TAG, String.valueOf(bg));
        if (!bg.equals(Integer.valueOf(PreferenceKeys.DEFAULT_SETTINGS_VALUE))) {
            TypedArray typedArray = getActivity().getResources().obtainTypedArray(R.array.background_resources);
            Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(bg, Integer.valueOf(PreferenceKeys.DEFAULT_SETTINGS_VALUE)));
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), backgroundImage);
            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.findViewById(R.id.chat_list_view).setBackground(bitmapDrawable);
            } else {
                view.findViewById(R.id.chat_list_view).setBackgroundDrawable(bitmapDrawable);
            }
            typedArray.recycle();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBound) {
            mApplicationContext.unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void messageArrived(Message message) {
        if(!init){
            mAdapter.add(message);
        }
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
        if (NetworkState.isConnected()) {
            if (form.isValid()) {
                etMessage.setText(StringResources.NULL_TEXT);
                mService.send(body);
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
        init = true;
        etMessage = (EditText) view.findViewById(R.id.new_message_edit_text);
        etMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    eLog.i(TAG, "Enter pressed");
                    onSend();
                    return true;
                }else{
                    return false;
                }
            }
        });
        final ListView lvChat = (ListView) view.findViewById(R.id.chat_list_view);
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        mMessages.clear();
        mAdapter = new MessageListAdapter(mApplicationContext, mMessages);
        lvChat.setAdapter(mAdapter);
        getChatHistory();
        lvChat.setEmptyView(view.findViewById(R.id.empty_message_layout));
        lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        init = false;
    }

    private void getChatHistory() {
        if (NetworkState.isConnected()) {
            Call<MessageGetResponse> getMessagesCall = ApiManager.getApiService().getMessages(
                    SessionManager.getCurrentUser().getAuthToken(),
                    CurrentActiveEvent.getInstance().getCurrentEvent().getEventID()
            );
            ApiManager.request(getMessagesCall, new NetworkCallback<MessageGetResponse>() {
                @Override
                public void onSuccess(MessageGetResponse response) {
                    eLog.i(TAG, "Fetched Chat History");
                    final List<Message> responseData = response.getData();
                    Collections.reverse(responseData);
                    mAdapter.addAll(responseData);
                }

                @Override
                public void onFail(FailedRequestResponse error) {
                    eLog.e(TAG, error.getMessage());
                }
            });
        } else {
            Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof NetworkState){
            if(NetworkState.isConnected()){
                getChatHistory();
                try {
                    mService.connect();
                } catch (MqttException e) {
                    eLog.e(TAG, e.getMessage());
                }
            }else{
                Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
            }
        }
    }
}
