package com.jaysyko.wrestlechat.network;

/**
 * NetworkResponseKeys.java
 *
 * Keys to parse the JSON Response
 * @author Jay Syko
 */
public enum NetworkResponseKeys {
    SUCCESS("success"),
    PAYLOAD("payload");

    private String key;

    NetworkResponseKeys(String key) {
        this.key = key;
    }

    /**
     * Convert Enum to String
     *
     * @return String value of enum
     */
    @Override
    public String toString() {
        return this.key;
    }
}
