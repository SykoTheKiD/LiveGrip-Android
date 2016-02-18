package com.jaysyko.wrestlechat.conversation;

import com.jaysyko.wrestlechat.models.Events;
import com.parse.ParseObject;

public class CurrentActiveEvent {
    private static CurrentActiveEvent event = new CurrentActiveEvent(null);
    ParseObject currentEvent;

    private CurrentActiveEvent(ParseObject currentEvent) {
        this.currentEvent = currentEvent;
    }

    public static CurrentActiveEvent getInstance() {
        return event;
    }

    public String getEventID() {
        return this.currentEvent.getObjectId();
    }

    public String getEventName() {
        return this.currentEvent.getString(Events.NAME);
    }

    public String getEventInfo() {
        return this.currentEvent.getString(Events.INFO);
    }

    public String getMatchCard() {
        return this.currentEvent.getString(Events.MATCH_CARD);
    }

    public String getEventImage() {
        return this.currentEvent.getString(Events.IMAGE);
    }

    public String getEventLocation() {
        return this.currentEvent.getString(Events.LOCATION);
    }

    public Long getEventStartTime() {
        return this.currentEvent.getLong(Events.START_TIME);
    }


    public void setCurrentEvent(ParseObject currentEvent) {
        this.currentEvent = currentEvent;
    }

}
