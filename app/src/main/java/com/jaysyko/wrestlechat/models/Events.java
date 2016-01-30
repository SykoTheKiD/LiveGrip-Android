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

    public String getEventIdKey() {
        return getString(EVENT_ID_KEY);
    }

    public String getEventNameKey() {
        return getString(EVENT_NAME_KEY);
    }

    public String getEventLocationKey() {
        return getString(EVENT_LOCATION_KEY);
    }

    public String getEventStartTimeKey() {
        return getString(EVENT_START_TIME_KEY);
    }

    public String getEventEndTimeKey() {
        return getString(EVENT_END_TIME_KEY);
    }

    public String getEventImageIdKey() {
        return getString(EVENT_IMAGE_ID_KEY);
    }
}
