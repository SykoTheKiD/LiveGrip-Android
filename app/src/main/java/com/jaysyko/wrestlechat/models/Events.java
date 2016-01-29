package com.jaysyko.wrestlechat.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName(Events.PARSE_EVENTS_TABLE)
public class Events extends ParseObject {
    public static final String PARSE_EVENTS_TABLE = "Events";
    public static final String EVENT_ID_KEY = "eventId";
    public static final String EVENT_NAME_KEY = "eventName";
    public static final String EVENT_LOCATION_KEY = "location";
    public static final String EVENT_START_TIME_KEY = "startTime";
    public static final String EVENT_END_TIME_KEY = "endTime";
    public static final String EVENT_IMAGE_ID_KEY = "imgurId";

}
