package com.jaysyko.wrestlechat.eventManager;

import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.query.Query;
import com.parse.ParseObject;

import java.util.List;

import static com.jaysyko.wrestlechat.db.BackEnd.queryCache;
import static com.jaysyko.wrestlechat.db.BackEnd.queryDB;

/**
 * Created by jarushaan on 2016-02-24.
 */
public class RetrieveEvents {
    private static final Class<Events> EVENT_MODEL = Events.class;
    private static final String EVENTS_MODEL_SIMPLE_NAME = EVENT_MODEL.getSimpleName();
    private static RetrieveEvents retrieveEvents = new RetrieveEvents();

    private List<ParseObject> eventList;

    private RetrieveEvents() {
    }

    public static RetrieveEvents getInstance() {
        return retrieveEvents;
    }

    public List<ParseObject> getEventList(Boolean level) {
        updateEventCards(level);
        return this.eventList;
    }

    //display clickable a list of all users
    @SuppressWarnings("unchecked")
    private synchronized void updateEventCards(Boolean hard) {
        Query query = new Query(EVENT_MODEL);
        query.orderByASC(Events.START_TIME);
        if (hard) {
            this.eventList = queryDB(query, EVENTS_MODEL_SIMPLE_NAME);
        } else {
            this.eventList = queryCache(query, EVENTS_MODEL_SIMPLE_NAME);
        }
    }
}
