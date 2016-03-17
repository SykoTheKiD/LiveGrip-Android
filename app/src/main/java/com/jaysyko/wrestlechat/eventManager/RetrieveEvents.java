package com.jaysyko.wrestlechat.eventManager;

import com.jaysyko.wrestlechat.db.QueryResult;
import com.jaysyko.wrestlechat.models.Event;

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

//    public List getEventList(Boolean level) {
//        updateEventCards(level);
//        return this.queryResult.getResults();
//    }

    //display clickable a list of all users
    @SuppressWarnings("unchecked")
    private synchronized void updateEventCards(Boolean hard) {
//        Query query = new Query(EVENT_MODEL);
//        query.orderByASC(DBConstants.EVENT_START_TIME_KEY);
//        if (hard) {
//            this.queryResult = execute(query, EVENTS_MODEL_SIMPLE_NAME);
//        } else {
//            this.queryResult = queryCache(query);
//        }
    }
}
