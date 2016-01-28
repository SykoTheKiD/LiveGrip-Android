package com.jaysyko.wrestlechat.models;

public class EventObject {
    private String eventName;
    private String location;
    private String startTime;
    private String endTime;

    public EventObject(String eventName, String location, String startTime, String endTime) {
        this.eventName = eventName;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEventName() {
        return this.eventName;
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
