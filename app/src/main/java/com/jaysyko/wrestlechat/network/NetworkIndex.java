package com.jaysyko.wrestlechat.network;

/**
 * Created by jarushaan on 2016-03-19
 */
public enum NetworkIndex {
    USERNAME("username"), PASSWORD("password"), SUCCESS("success"), PAYLOAD("payload");

    private String key;

    NetworkIndex(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
