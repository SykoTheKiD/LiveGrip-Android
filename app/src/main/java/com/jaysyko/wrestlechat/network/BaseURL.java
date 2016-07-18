package com.jaysyko.wrestlechat.network;

import com.jaysyko.wrestlechat.application.App;
import com.jaysyko.wrestlechat.utils.StringResources;

/**
 * URLS.java
 * Development
 *
 * @author Jay Syko
 */
public final class BaseURL {
    private static final String DEVELOPMENT_SERVER_URL = "http://192.168.0.12:3000/";
    private static final String DEVELOPMENT_MOSQUITTO_PROTOCOL = "tcp://";
    private static final String DEVELOPMENT_MOSQUITTO_URL = "192.168.0.12";
    private static final String DEVELOPMENT_MOSQUITTO_PORT = "8081";
    private static final String DEVELOPMENT_MOSQUITTO = DEVELOPMENT_MOSQUITTO_PROTOCOL + DEVELOPMENT_MOSQUITTO_URL + StringResources.COLON + DEVELOPMENT_MOSQUITTO_PORT;

    private static final String PRODUCTION_SERVER_URL = "";
    private static final String PRODUCTION_MOSQUITTO_PROTOCOL = "tcp://";
    private static final String PRODUCTION_MOSQUITTO_URL = "10.0.3.2";
    private static final String PRODUCTION_MOSQUITTO_PORT = "8001";
    private static final String PRODUCTION_MOSQUITTO = PRODUCTION_MOSQUITTO_PROTOCOL + PRODUCTION_MOSQUITTO_URL + StringResources.COLON + PRODUCTION_MOSQUITTO_PORT;

    public static String getServerURL() {
        return (App.debug) ? DEVELOPMENT_SERVER_URL : PRODUCTION_SERVER_URL;
    }

    public static String getMosquittoURL() {
        return (App.debug) ? DEVELOPMENT_MOSQUITTO : PRODUCTION_MOSQUITTO;
    }

}