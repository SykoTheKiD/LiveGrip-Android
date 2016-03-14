package com.jaysyko.wrestlechat.eventManager;

import com.jaysyko.wrestlechat.db.QueryResult;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.db.Query;

import java.util.List;

import static com.jaysyko.wrestlechat.db.BackEnd.queryCache;
import static com.jaysyko.wrestlechat.db.BackEnd.queryDB;

/**
 * RetrieveEvents.java
 * Get and hold a list of all the events
 *
 * @author Jay Syko
 */
public class RetrieveEvents {
    private static final Class<Event> EVENT_MODEL = Event.class;
    private static final String EVENTS_MODEL_SIMPLE_NAME = EVENT_MODEL.getSimpleName();
    private static RetrieveEvents retrieveEvents = new RetrieveEvents();

    private QueryResult queryResult;

    private RetrieveEvents() {
    }

    public static RetrieveEvents getInstance() {
        return retrieveEvents;
    }

    public List getEventList(Boolean level) {
        updateEventCards(level);
        return this.queryResult.getResults();
    }

    //display clickable a list of all users
    @SuppressWarnings("unchecked")
    private synchronized void updateEventCards(Boolean hard) {
        Query query = new Query(EVENT_MODEL);
        query.orderByASC(Event.START_TIME);
        if (hard) {
            this.queryResult = queryDB(query, EVENTS_MODEL_SIMPLE_NAME);
        } else {
            this.queryResult = queryCache(query);
        }
    }
}
