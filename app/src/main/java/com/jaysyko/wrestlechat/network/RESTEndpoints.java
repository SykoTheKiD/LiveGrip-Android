package com.jaysyko.wrestlechat.network;

/**
 * RESTEndpoints.java
 * All the endpoints provided by the Backend API
 * @author Jay Syko
 */
public enum RESTEndpoints {
    LOGIN("login"), SIGN_UP("newuser"), MESSAGES("messages"), EVENTS("events"), GCM("gcmID"), UPDATE_PROFILE("updateProfile");

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
