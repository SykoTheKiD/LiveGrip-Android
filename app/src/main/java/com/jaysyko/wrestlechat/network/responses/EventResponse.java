package com.jaysyko.wrestlechat.network.responses;

import com.google.gson.annotations.SerializedName;
import com.jaysyko.wrestlechat.models.Event;

import java.util.List;


public class EventResponse {

    @SerializedName(JSONKeys.STATUS)
    private String status;
    @SerializedName(JSONKeys.MESSAGE)
    private String message;
    @SerializedName(JSONKeys.PAYLOAD)
    private List<Event> events;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
