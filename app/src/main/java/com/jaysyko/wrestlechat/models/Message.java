package com.jaysyko.wrestlechat.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName(Message.PARSE_MESSAGE_TABLE)
public class Message extends ParseObject {

    public static final String PARSE_MESSAGE_TABLE = "Message";
    public static final String USERNAME = "username";
    public static final String EVENT_ID = "eventId";
    public static final String MSG_BODY = "body";
    public static final String CREATED_AT = "createdAt";

    public String getUsername() {
        return getString(USERNAME);
    }

    public void setUsername(String username) {
        put(USERNAME, username);
    }

    public void setEventId(String eventId) {
        put(EVENT_ID, eventId);
    }

    public String getBody() {
        return getString(MSG_BODY);
    }

    public void setBody(String body) {
        put(MSG_BODY, body);
    }
}