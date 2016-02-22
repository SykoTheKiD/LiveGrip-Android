package com.jaysyko.wrestlechat.dataObjects;

/**
 * EventObject.java
 * EventObject holder for use in the EventList adapter
 *
 * @author Jay Syko
 */

public class EventObject {
    private String eventName;
    private String location;
    private Long startTime;
    private Long endTime;
    private String imageLink;

    public EventObject(String eventName, String location, Long startTime, Long endTime, String imageLink) {
        this.eventName = eventName;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.imageLink = imageLink;
    }

    /**
     * Returns the event's name
     * @return eventName: String
     */
    public String getEventName() {
        return this.eventName;
    }

    /**
     * Returns the event's image
     * @return imageURL : String
     */
    public String getImageLink() {
        return this.imageLink;
    }

    /**
     * Returns the event's location
     * @return location: String
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Returns the event's startTime in millis
     * @return startTime: Long
     */
    public Long getStartTime() {
        return this.startTime;
    }

    /**
     * Returns the event's endTime in millis
     * @return endTime: Long
     */
    public Long getEndTime() {
        return this.endTime;
    }
}
