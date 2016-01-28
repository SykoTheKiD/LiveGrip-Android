package com.jaysyko.wrestlechat.models.db;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName(ParseMessageModel.PARSE_MESSAGE_TABLE)
public class ParseMessageModel extends ParseObject {

    public static final String PARSE_MESSAGE_TABLE = "Message";
    public static final String USER_ID_KEY = "userId";
    public static final String EVENT_ID_KEY = "eventId";
    public static final String MSG_BODY_KEY = "body";

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