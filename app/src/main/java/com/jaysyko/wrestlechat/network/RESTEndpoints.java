package com.jaysyko.wrestlechat.network;

/**
 * RESTEndpoints.java
 * All the endpoints provided by the Backend API
 * @author Jay Syko
 */
public enum RESTEndpoints {
    LOGIN("auth/login"), SIGN_UP("auth/newuser"), MESSAGES("messages/save_message"), EVENTS("events/events"), GCM("gcm/gcmID"), UPDATE_PROFILE("auth/updateProfile");

    private static final String FILE_TYPE = ".php";
    private String endpoint;

    RESTEndpoints(String endpoint) {
        this.endpoint = endpoint.concat(FILE_TYPE);
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
