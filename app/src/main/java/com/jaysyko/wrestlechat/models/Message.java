package com.jaysyko.wrestlechat.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName(Message.PARSE_MESSAGE_TABLE)
public class Message extends ParseObject {

    public static final String PARSE_MESSAGE_TABLE = "Message";
    public static final String USER_ID_KEY = "userId";
    public static final String USERNAME_KEY = "username";
    public static final String EVENT_ID_KEY = "eventId";
    public static final String MSG_BODY_KEY = "body";
    public static final String CREATED_AT_KEY = "createdAt";

    public String getUsername() {
        return getString(USERNAME_KEY);
    }

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public String getEventId() {
        return getString(EVENT_ID_KEY);
    }

    public void setEventId(String eventId) {
        put(EVENT_ID_KEY, eventId);
    }

    public String getBody() {
        return getString(MSG_BODY_KEY);
    }

    public void setBody(String body) {
        put(MSG_BODY_KEY, body);
    }
}