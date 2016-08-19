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

    private static boolean genymotion = false;

    // Development client URLS
    private static final String GENYMOTION_DEVELOPMENT_SERVER_IP = "10.0.3.2";
    private static final String DEVELOPMENT_SERVER_IP = "192.168.0.10";

    // Client URL config
    private static String devServer = (genymotion) ? GENYMOTION_DEVELOPMENT_SERVER_IP : DEVELOPMENT_SERVER_IP;
    private static final String DEVELOPMENT_SERVER_URL = "http://" + devServer +":3000/";

    // Development Mosquitto Config
    private static final String DEVELOPMENT_MOSQUITTO_PROTOCOL = "tcp://";
    private static final String DEVELOPMENT_MOSQUITTO_URL = devServer;
    private static final String DEVELOPMENT_MOSQUITTO_PORT = "8081";
    private static final String DEVELOPMENT_MOSQUITTO = DEVELOPMENT_MOSQUITTO_PROTOCOL + DEVELOPMENT_MOSQUITTO_URL + StringResources.COLON + DEVELOPMENT_MOSQUITTO_PORT;


    // Production URL Config
    private static final String PRODUCTION_SERVER_IP = "159.203.29.63";
    private static final String PRODUCTION_SERVER_URL = "http://" + PRODUCTION_SERVER_IP + ":3000/";

    // Production Mosquitto Config
    private static final String PRODUCTION_MOSQUITTO_PROTOCOL = "tcp://";
    private static final String PRODUCTION_MOSQUITTO_URL = PRODUCTION_SERVER_IP;
    private static final String PRODUCTION_MOSQUITTO_PORT = "8081";
    private static final String PRODUCTION_MOSQUITTO = PRODUCTION_MOSQUITTO_PROTOCOL + PRODUCTION_MOSQUITTO_URL + StringResources.COLON + PRODUCTION_MOSQUITTO_PORT;

    private static boolean debug = App.debug;

    public static String getServerURL() {
        return (debug) ? DEVELOPMENT_SERVER_URL : PRODUCTION_SERVER_URL;
    }

    public static String getMosquittoURL() {
        return (debug) ? DEVELOPMENT_MOSQUITTO : PRODUCTION_MOSQUITTO;
    }
}