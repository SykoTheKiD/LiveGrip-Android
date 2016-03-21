package com.jaysyko.wrestlechat.network;

/**
 * Created by jarushaan on 2016-03-21
 */
public enum NetworkResponseKeys {
    SUCCESS("success"),
    PAYLOAD("payload");

    private String key;

    NetworkResponseKeys(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
