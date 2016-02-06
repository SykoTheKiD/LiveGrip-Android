package com.jaysyko.wrestlechat.dataObjects;

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

    public String getEventName() {
        return this.eventName;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public String getLocation() {
        return this.location;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }
}
