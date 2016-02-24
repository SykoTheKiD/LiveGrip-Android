package com.jaysyko.wrestlechat.eventManager;

import com.jaysyko.wrestlechat.dataObjects.EventObject;
import com.jaysyko.wrestlechat.models.Events;
import com.jaysyko.wrestlechat.query.Query;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarushaan on 2016-02-24.
 */
public class RetrieveEvents {
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
        ArrayList<EventObject> eventObjects = new ArrayList<>();
        Query query = new Query(Events.class);
        query.orderByASC(Events.START_TIME);
        if (hard) {
            this.eventList = query.executeHard();
        } else {
            this.eventList = query.execute();
        }
    }
}
