package com.jaysyko.wrestlechat.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName(Events.PARSE_EVENTS_TABLE)
public class Events extends ParseObject {
    public static final String PARSE_EVENTS_TABLE = "Events";
    public static final String EVENT_ID = "eventId";
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_LOCATION = "location";
    public static final String EVENT_START_TIME = "startTime";
    public static final String EVENT_END_TIME = "endTime";
    public static final String EVENT_IMAGE = "imgurId";
    public static final String EVENT_INFO = "eventInfo";
    public static final String EVENT_MATCH_CARD = "matchCard";

}
