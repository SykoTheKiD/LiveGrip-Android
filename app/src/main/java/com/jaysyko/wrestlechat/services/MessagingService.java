package com.jaysyko.wrestlechat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.db.Query;
import com.jaysyko.wrestlechat.db.QueryResult;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.utils.DBConstants;

import java.util.Collections;
import java.util.List;

import static com.jaysyko.wrestlechat.db.BackEnd.queryDB;

/**
 * Created by jarushaan on 2016-03-09
 */
public class MessagingService extends Service {
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private final IBinder mBinder = new MessageBinder(this);
    private Handler handler = new Handler();
    private List messageList;
    private final Runnable fetchMessageRunnable = new Runnable() {
        @Override
        public void run() {
            fetchOldMessages();
        }
    };

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
            Query query = new Query(Message.class);
            String sEventId = CurrentActiveEvent.getInstance().getCurrentEvent().getEventID();
            query.whereEqualTo(DBConstants.EVENT_ID_KEY, sEventId);
            query.orderByDESC(DBConstants.MESSAGE_CREATED_AT_KEY);
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            QueryResult messages = queryDB(query);
            messageList = messages != null ? messages.getResults() : null;
            if (messageList != null) {
                Collections.reverse(messageList);
            }
        }
    }

    public List getMessageList() {
        return this.messageList;
    }
}
