package com.jaysyko.wrestlechat.services;

import android.app.IntentService;
import android.content.Intent;

import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.network.NetworkState;
import com.jaysyko.wrestlechat.query.Query;

import java.util.Collections;
import java.util.List;

import static com.jaysyko.wrestlechat.db.BackEnd.queryDB;

/**
 * Created by jarushaan on 2016-03-09.
 */
public class MessagingService extends IntentService {
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    private static final String CLASS_NAME = MessagingService.class.getSimpleName();
    private final String sEventId = CurrentActiveEvent.getInstance().getEventID();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MessagingService() {
        super(CLASS_NAME);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onHandleIntent(Intent intent) {
        if (NetworkState.isConnected(getApplicationContext())) {
            Query query = new Query(Message.class);
            query.whereEqualTo(Events.ID, sEventId);
            query.orderByDESC(Message.CREATED_AT);
            query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
            List messages = queryDB(query, Message.class.getSimpleName());
            if (messages != null) {
                Collections.reverse(messages);
//                MessagingFragment.updateMessages(messages);
            }
        }
    }
}
