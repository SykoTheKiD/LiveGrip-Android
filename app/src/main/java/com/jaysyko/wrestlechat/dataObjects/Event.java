package com.jaysyko.wrestlechat.dataObjects;

public class Event {
    private String eventName;
    private String location;
    private String startTime;
    private String endTime;
    private String imageLink;

    public Event(String eventName, String location, String startTime, String endTime, String imageLink) {
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

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }
}
