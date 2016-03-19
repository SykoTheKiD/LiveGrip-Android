package com.jaysyko.wrestlechat.models;

/**
 * Created by jarushaan on 2016-03-19
 */
public enum EventJSONKeys {
    ID("id"), NAME("name"), INFO("info"), MATCH_CARD("match_card"), IMAGE("image"), LOCATION("location"), START_TIME("start_time"), END_TIME("end_time");
    private String key;

    EventJSONKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
