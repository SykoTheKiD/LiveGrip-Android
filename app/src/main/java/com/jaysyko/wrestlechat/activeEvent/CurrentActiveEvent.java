package com.jaysyko.wrestlechat.activeEvent;

import com.jaysyko.wrestlechat.models.Events;
import com.parse.ParseObject;

/**
 * CurrentActiveEvent.java
 * Holder class for the current event in the user's focus
 * @author Jay Syko
 */
public class CurrentActiveEvent {
    private static CurrentActiveEvent event = new CurrentActiveEvent(null);
    private ParseObject currentEvent;

    private CurrentActiveEvent(ParseObject currentEvent) {
        this.currentEvent = currentEvent;
    }

    /**
     * Return a single instance of the CurrentEvent object
     * @return CurrentActiveEvent
     */
    public static CurrentActiveEvent getInstance() {
        return event;
    }

    /**
     * Returns the current event's ID
     * @return String
     */
    public String getEventID() {
        return this.currentEvent.getObjectId();
    }

    /**
     * Returns the current event's name
     * @return String
     */
    public String getEventName() {
        return this.currentEvent.getString(Events.NAME);
    }

    /**
     * Returns the current event's information
     * @return String
     */
    public String getEventInfo() {
        return this.currentEvent.getString(Events.INFO);
    }

    /**
     * Return the match card for the current event
     * @return String
     */
    public String getMatchCard() {
        return this.currentEvent.getString(Events.MATCH_CARD);
    }

    /**
     * Returns the event's image URL
     * @return String
     */
    public String getEventImage() {
        return this.currentEvent.getString(Events.IMAGE);
    }

    /**
     * Returns the event's location
     * @return String
     */
    public String getEventLocation() {
        return this.currentEvent.getString(Events.LOCATION);
    }

    /**
     * Returns the event's startTime
     * @return Long
     */
    public Long getEventStartTime() {
        return this.currentEvent.getLong(Events.START_TIME);
    }

    /**
     * Sets the currentEvent in focus
     * @param currentEvent void
     */
    public void setCurrentEvent(ParseObject currentEvent) {
        this.currentEvent = currentEvent;
    }

}
