package com.jaysyko.wrestlechat.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.services.MessagingService;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.ArrayList;

public class MessagingFragment extends Fragment {

    private static final int SEND_DELAY = 1500;
    private String userName, sEventId;
    private EditText etMessage;
    private ImageButton btSend;
    private Context applicationContext;
    private View view;
    private Handler handler = new Handler();
    private ArrayList<Message> messages = new ArrayList<>();
    private Runnable initMessageAdapter = new Runnable() {
        @Override
        public void run() {
            initMessageAdapter();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String eventName = CurrentActiveEvent.getInstance().getEventName();
        getActivity().setTitle(eventName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messaging, container, false);
        applicationContext = getActivity();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) applicationContext).setSupportActionBar(toolbar);
        sEventId = CurrentActiveEvent.getInstance().getEventID();
        CurrentActiveUser currentUser = CurrentActiveUser.getInstance();
        userName = currentUser.getUsername();
        btSend = (ImageButton) view.findViewById(R.id.btSend);
        handler.post(initMessageAdapter);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSend.setEnabled(false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String body = etMessage.getText().toString().trim();
                        Form form = new MessageValidator(body).validate();
                        if (NetworkState.isConnected(applicationContext)) {
                            if (form.isValid()) {
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
        ListView lvChat = (ListView) view.findViewById(R.id.lvChat);
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode(1);
        MessageListAdapter mAdapter = new MessageListAdapter(applicationContext, userName, messages);
        lvChat.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(applicationContext, MessagingService.class);
        applicationContext.startService(intent);
    }

//    public static void updateMessages(List<Message> newMessages){
//        messages.clear();
//        messages.addAll(newMessages);
//        mAdapter.notifyDataSetChanged();
//    }
}
