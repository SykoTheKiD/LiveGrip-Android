package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activities.MessagingActivity;
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
import com.jaysyko.wrestlechat.services.ServiceProvider;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceKeys;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.ArrayList;

import retrofit2.Call;

public class MessagingFragment extends Fragment implements IMessageArrivedListener {

    private static final String TAG = MessagingFragment.class.getSimpleName();
    private static final int SEND_DELAY = 1500;
    private static final String FONT_COLOR_HTML = "<font color=\"#FFFFFFF\">", FONT_HTML = "</font>";
    private View view;
    private boolean init;
    private Button btSend;
    private EditText etMessage;
    private Context mApplicationContext;
    private Handler handler = new Handler();
    private ServiceProvider serviceProvider;
    private static MessageListAdapter mAdapter;
    private static ArrayList<Message> mMessages = new ArrayList<>();
    private Runnable initMessageAdapter = new Runnable() {
        @Override
        public void run() {
            initMessageAdapter();
        }
    };
    private Event mCurrentEvent = CurrentActiveEvent.getInstance().getCurrentEvent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.setTitle(Html.fromHtml(FONT_COLOR_HTML + mCurrentEvent.getEventName() + FONT_HTML));
        serviceProvider = (MessagingActivity) getActivity();
        serviceProvider.getMessagingServiceBinder().setMessageArrivedListener(this);
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
                etMessage.setText(StringResources.NULL_TEXT);
                serviceProvider.getMessagingService().send(body);
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
        etMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    eLog.i(TAG, "Enter pressed");
                    onSend();
                    return true;
                }
                return false;
            }
        });
        final ListView lvChat = (ListView) view.findViewById(R.id.chat_list_view);
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        mAdapter = new MessageListAdapter(mApplicationContext, mMessages);
        lvChat.setAdapter(mAdapter);
        getChatHistory();
        lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvChat.setEmptyView(view.findViewById(R.id.empty_message_layout));
        lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        init = false;
    }

    @Override
    public void messageArrived(Message message) {
        if(!init){
            mAdapter.add(message);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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

    private void getChatHistory() {
        if (NetworkState.isConnected(mApplicationContext)) {
            Call<MessageGetResponse> getMessagesCall = ApiManager.getApiService().getMessages(
                    SessionManager.getCurrentUser().getAuthToken(),
                    CurrentActiveEvent.getInstance().getCurrentEvent().getEventID()
            );
            ApiManager.request(getMessagesCall, new NetworkCallback<MessageGetResponse>() {
                @Override
                public void onSuccess(MessageGetResponse response) {
                    eLog.i(TAG, "Fetched Chat History");
                    mAdapter.addAll(response.getData());
                }

                @Override
                public void onFail(FailedRequestResponse error) {
                    error.getCode(mApplicationContext);
                    eLog.e(TAG, error.getMessage());
                }
            });
        } else {
            Dialog.makeToast(mApplicationContext, getString(R.string.no_network));
        }
    }
}
