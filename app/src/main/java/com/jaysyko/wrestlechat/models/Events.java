package com.jaysyko.wrestlechat.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName(Events.PARSE_EVENTS_TABLE)
public class Events extends ParseObject {
    public static final String PARSE_EVENTS_TABLE = "Events";
    public static final String ID = "eventId";
    public static final String NAME = "eventName";
    public static final String LOCATION = "location";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String IMAGE = "imgurId";
    public static final String INFO = "eventInfo";
    public static final String MATCH_CARD = "matchCard";

}
