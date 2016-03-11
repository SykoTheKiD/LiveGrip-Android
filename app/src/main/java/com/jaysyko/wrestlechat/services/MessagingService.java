package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private static final String CLASS_NAME = MessagingService.class.getSimpleName();
    private final IBinder mBinder = new LocalMessageBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        fetchNewMessages();
        return mBinder;
    }

    @SuppressWarnings("unchecked")
    private void fetchNewMessages() {
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

    public class LocalMessageBinder extends Binder {
        public MessagingService getService() {
            return MessagingService.this;
        }
    }
}
