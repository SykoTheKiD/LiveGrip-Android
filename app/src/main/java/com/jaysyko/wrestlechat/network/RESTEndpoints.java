package com.jaysyko.wrestlechat.network;

/**
 * Created by jarushaan on 2016-03-18
 */
public enum RESTEndpoints {
    LOGIN("login"), SIGN_UP("newuser"), MESSAGES("messages"), EVENTS("events"), GCM("gcmID"), UPDATE_PROFILE("updateProfile");

    private static final String FILE_TYPE = ".php";
    private String endpoint;

    RESTEndpoints(String endpoint) {
        this.endpoint = endpoint.concat(FILE_TYPE);
    }

    public String getEndpoint() {
        return this.endpoint;
    }
}
