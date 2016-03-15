package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.db.QueryResult;
import com.jaysyko.wrestlechat.fragments.MessagingFragment;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.db.Query;

import java.util.Collections;
import java.util.List;

import static com.jaysyko.wrestlechat.db.BackEnd.queryDB;

/**
 * Created by jarushaan on 2016-03-09
 */
public class MessagingService extends Service {
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private final Runnable fetchMessageRunnable = new Runnable() {
        @Override
        public void run() {
            fetchOldMessages();
        }
    };
    private Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler.post(fetchMessageRunnable);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @SuppressWarnings("unchecked")
    private void fetchOldMessages() {
        if (NetworkState.isConnected(getApplicationContext())) {
            Query query = new Query(Message.class);
            String sEventId = CurrentActiveEvent.getInstance().getCurrentEvent().getEventID();
            query.whereEqualTo(Event.ID, sEventId);
            query.orderByDESC(Message.CREATED_AT);
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            QueryResult messages = queryDB(query);
            List messageList = messages != null ? messages.getResults() : null;
            if (messageList != null) {
                Collections.reverse(messageList);
                MessagingFragment.update(messageList);
            }
        }
    }
}
