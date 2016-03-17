package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jaysyko.wrestlechat.network.NetworkState;

import java.util.List;

/**
 * Created by jarushaan on 2016-03-09
 */
public class MessagingService extends Service {
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private final IBinder mBinder = new MessageBinder(this);
    private final Runnable fetchMessageRunnable = new Runnable() {
        @Override
        public void run() {
            fetchOldMessages();
        }
    };
    private Handler handler = new Handler();
    private List messageList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
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
//            Query query = new Query(Message.class);
//            String sEventId = CurrentActiveEvent.getInstance().getCurrentEvent().getEventID();
//            query.whereEqualTo(DBConstants.EVENT_ID_KEY, sEventId);
//            query.orderByDESC(DBConstants.MESSAGE_CREATED_AT_KEY);
//            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
//            QueryResult messages = execute(query);
//            messageList = messages != null ? messages.getResults() : null;
//            if (messageList != null) {
//                Collections.reverse(messageList);
//            }
        }
    }

    public List getMessageList() {
        return this.messageList;
    }
}
