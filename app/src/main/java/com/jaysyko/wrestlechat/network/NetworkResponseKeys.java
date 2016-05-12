package com.jaysyko.wrestlechat.network;

/**
 * NetworkResponseKeys.java
 *
 * Keys to parse the JSON Response
 * @author Jay Syko
 */
public enum NetworkResponseKeys {
    STATUS("status"),
    MESSAGE("message"),
    DATA("data");

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
