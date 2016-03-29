package com.jaysyko.wrestlechat.auth;

/**
 * Created by jarushaan on 2016-03-19
 */
public enum UserKeys {
    USERNAME("username"),
    PASSWORD("password"),
    ID("id"),
    PROFILE_IMAGE("profile_image"),
    GCM("gcm_id");

    private String key;

    UserKeys(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
