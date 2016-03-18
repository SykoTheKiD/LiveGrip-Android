package com.jaysyko.wrestlechat.models;

/**
 * Events.java
 * Model for the Event object in the database
 * Contains all the keys in the database table
 *
 * @author Jay Syko
 */

public class Event {

    private String eventID, eventName, eventInfo, matchCard, eventImage, eventLocation;
    private String eventStartTime, eventEndTime;

    public Event(String eventID, String eventName, String eventInfo, String matchCard, String eventImage, String eventLocation, String eventStartTime, String eventEndTime) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventInfo = eventInfo;
        this.matchCard = matchCard;
        this.eventImage = eventImage;
        this.eventLocation = eventLocation;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }


    /**
     * Returns the current event's EVENT_ID_KEY
     *
     * @return String
     */
    public String getEventID() {
        return this.eventID;
    }

    /**
     * Returns the current event's name
     *
     * @return String
     */
    public String getEventName() {
        return this.eventName;
    }

    /**
     * Returns the current event's information
     *
     * @return String
     */
    public String getEventInfo() {
        return this.eventInfo;
    }

    /**
     * Return the match card for the current event
     *
     * @return String
     */
    public String getMatchCard() {
        return this.matchCard;
    }

    /**
     * Returns the event's image URL
     *
     * @return String
     */
    public String getEventImage() {
        return this.eventImage;
    }

    /**
     * Returns the event's location
     *
     * @return String
     */
    public String getEventLocation() {
        return this.eventLocation;
    }

    /**
     * Returns the event's startTime
     *
     * @return Long
     */
    public String getEventStartTime() {
        return this.eventStartTime;
    }

    /**
     * @return Long
     */
    public String getEventEndTime() {
        return this.eventEndTime;
    }
}
