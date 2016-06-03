package com.jaysyko.wrestlechat.network;

/**
 * RESTEndpoints.java
 * All the endpoints provided by the Backend API
 * @author Jay Syko
 */
public enum RESTEndpoints {
    LOGIN("auth/login"),
    SIGN_UP("auth/register"),
    MESSAGES("messages/save"),
    EVENTS("events"),
    GCM("user/update/gcm_id"),
    UPDATE_PROFILE_IMAGE("user/update/profile_image");

    private String endpoint;

    RESTEndpoints(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Return a string of the REST Endpoint Enum
     *
     * @return String
     */
    public String getEndpoint() {
        return this.endpoint;
    }
}
