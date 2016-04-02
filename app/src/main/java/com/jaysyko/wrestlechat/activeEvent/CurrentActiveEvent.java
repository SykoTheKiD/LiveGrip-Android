package com.jaysyko.wrestlechat.activeEvent;

import com.jaysyko.wrestlechat.models.Event;

/**
 * CurrentActiveEvent.java
 * Holder class for the current event in the user's focus
 * @author Jay Syko
 */
public class CurrentActiveEvent {
    private static CurrentActiveEvent event = new CurrentActiveEvent(null);
    private Event currentEvent;

    private CurrentActiveEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    /**
     * Return a single instance of the CurrentEvent object
     * @return CurrentActiveEvent
     */
    public static CurrentActiveEvent getInstance() {
        return event;
    }

    public Event getCurrentEvent() {
        return this.currentEvent;
    }

    /**
     * Sets the currentEvent in focus
     * @param currentEvent Event
     */
    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

}
