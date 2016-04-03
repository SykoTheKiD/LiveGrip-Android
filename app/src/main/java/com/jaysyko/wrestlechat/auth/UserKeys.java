package com.jaysyko.wrestlechat.auth;

/**
 * @author Jay Syko
 */
public enum UserKeys {
    USERNAME("username"),
    PASSWORD("password"),
    ID("id"),
    PROFILE_IMAGE("profile_image"),
    GCM("gcm_id"),
    APP_VERSION("app_version");

    private String key;

    UserKeys(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
