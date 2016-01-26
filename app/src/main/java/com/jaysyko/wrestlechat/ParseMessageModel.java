package com.jaysyko.wrestlechat;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class ParseMessageModel extends ParseObject {

    public String getUserId() {
        return getString("userId");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public String getEventId() {
        return getString("eventId");
    }

    public void setEventId(String eventId) {
        put("eventId", eventId);
    }

    public String getBody() {
        return getString("body");
    }

    public void setBody(String body) {
        put("body", body);
    }
}