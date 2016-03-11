package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.fragments.MessagingFragment;
import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.query.Query;

import java.util.Collections;
import java.util.List;

import static com.jaysyko.wrestlechat.db.BackEnd.queryDB;

/**
 * Created by jarushaan on 2016-03-09
 */
public class MessagingService extends Service {
    private static final int FETCH_MSG_DELAY_MILLIS = 1000, MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private final IBinder mBinder = new LocalMessageBinder(this);
    private Handler handler = new Handler();
    private final Runnable fetchMessageRunnable = new Runnable() {
        @Override
        public void run() {
            fetchNewMessages();
            handler.postDelayed(this, FETCH_MSG_DELAY_MILLIS);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        handler.post(fetchMessageRunnable);
        return mBinder;
    }

    @SuppressWarnings("unchecked")
    private void fetchNewMessages() {
        Log.e("HERE", "FETCHED");
        if (NetworkState.isConnected(getApplicationContext())) {
            Query query = new Query(Message.class);
            String sEventId = CurrentActiveEvent.getInstance().getEventID();
            query.whereEqualTo(Events.ID, sEventId);
            query.orderByDESC(Message.CREATED_AT);
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            List messages = queryDB(query, Message.class.getSimpleName());
            if (messages != null) {
                Collections.reverse(messages);
                MessagingFragment.update(messages);
            }
        }
    }

}
