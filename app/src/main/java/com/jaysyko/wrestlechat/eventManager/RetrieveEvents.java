package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;

import com.android.volley.toolbox.StringRequest;
import com.jaysyko.wrestlechat.db.BackEnd;
import com.jaysyko.wrestlechat.models.Event;

import java.util.ArrayList;
import java.util.List;

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
    private List<Event> eventsList = new ArrayList<>();
    private Context context;
    private StringRequest stringRequest;

    private RetrieveEvents() {
    }

    public static RetrieveEvents getInstance(Context context, StringRequest stringRequest) {
        retrieveEvents.context = context;
        retrieveEvents.stringRequest = stringRequest;
        return retrieveEvents;
    }

    public List<Event> getEventList() {
        updateEventCards();
        return retrieveEvents.eventsList;
    }

    public synchronized void updateEventCards() {
        new BackEnd(context).execute(stringRequest);
    }
}
