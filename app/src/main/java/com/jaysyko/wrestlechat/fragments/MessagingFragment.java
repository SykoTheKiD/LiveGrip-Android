package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
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
import com.jaysyko.wrestlechat.dialogs.Dialog;
import com.jaysyko.wrestlechat.eventManager.CurrentActiveEvent;
import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.formTypes.MessagingForm;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.services.IMessageArrivedListener;
import com.jaysyko.wrestlechat.services.MessagingService;
import com.jaysyko.wrestlechat.services.MessagingServiceBinder;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.ArrayList;

public class MessagingFragment extends Fragment implements IMessageArrivedListener {

    private static final String TAG = MessagingFragment.class.getSimpleName();
    private static final int SEND_DELAY = 1500;
    private static final String FONT_COLOR_HTML = "<font color=\"#FFFFFFF\">";
    private static final String FONT_HTML = "</font>";
    private static final String DEFAULT_SETTINGS_VALUE = "0";
    private static final String MESSAGING_WALLPAPER = "messagingWallpaper";
    private static ArrayList<Message> mMessages = new ArrayList<>();
    private static MessageListAdapter mAdapter;
    private EditText etMessage;
    private Button btSend;
    private Context mApplicationContext;
    private View view;
    private Handler handler = new Handler();
    private boolean mServiceBound = false;
    private int mCurrentEventId = CurrentActiveEvent.getInstance().getCurrentEvent().getEventID();
    private Intent mChatServiceIntent;
    private MessagingService messagingService;
    private SharedPreferences sharedPreferences;
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
//            mMessagingServiceBinder.getService().disconnect();
            mServiceBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.setTitle(Html.fromHtml(FONT_COLOR_HTML + mCurrentEvent.getEventName() + FONT_HTML));
        mChatServiceIntent = new Intent(activity, MessagingService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messaging, container, false);
        mApplicationContext = getActivity();
//        getActivity().getWindow().setBackgroundDrawable(null);
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
        try {
            final MediaPlayer mp = MediaPlayer.create(mApplicationContext, R.raw.music_marimba_chord);
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
            }
            mp.start();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void saveMessage(String body) {
        Form form = new MessagingForm(body).validate();
        if (NetworkState.isConnected(mApplicationContext)) {
            if (form.isValid()) {
//                messagingService.send(body);
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
//        mApplicationContext.bindService(mChatServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
//        if (!mServiceBound) {
//            getActivity().startService(mChatServiceIntent);
//        }
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        Integer bg = Integer.parseInt(settings.getString(MESSAGING_WALLPAPER, DEFAULT_SETTINGS_VALUE));
//        if (!bg.equals(Integer.valueOf(DEFAULT_SETTINGS_VALUE))) {
//            TypedArray typedArray = getActivity().getResources().obtainTypedArray(R.array.background_resources);
//            Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(bg, Integer.valueOf(DEFAULT_SETTINGS_VALUE)));
//            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), backgroundImage);
//            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                view.findViewById(R.id.chat_list_view).setBackground(bitmapDrawable);
//            }
//            typedArray.recycle();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceBound) {
//            mMessagingServiceBinder.getService().disconnect();
            getActivity().unbindService(mServiceConnection);
            getActivity().stopService(mChatServiceIntent);
            mServiceBound = false;
        }
    }
}
