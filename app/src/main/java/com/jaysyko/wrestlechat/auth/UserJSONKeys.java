package com.jaysyko.wrestlechat.auth;

/**
 * Created by jarushaan on 2016-03-19
 */
public enum UserJSONKeys {
    USERNAME("username"),
    PASSWORD("password"),
    SUCCESS("success"),
    PAYLOAD("payload"),
    ID("id"),
    PROFILE_IMAGE("profile_image");

    private String key;

    UserJSONKeys(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
