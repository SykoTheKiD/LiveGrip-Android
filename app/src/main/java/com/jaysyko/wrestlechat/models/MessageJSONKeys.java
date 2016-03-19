package com.jaysyko.wrestlechat.models;

/**
 * Created by jarushaan on 2016-03-19
 */
public enum MessageJSONKeys {
    USERNAME("username"), EVENT_NAME("name"), BODY("body"), PROFILE_IMAGE("profile_image"), EVENT_ID("event_id");
    private String key;

    MessageJSONKeys(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}