package com.jaysyko.wrestlechat.network;

/**
 * URLS.java
 * Development
 *
 * @author Jay Syko
 */
public final class URLS {
    private static final String DEVELOPMENT_SERVER_URL = "127.0.0.1:3000/";
    private static final String DEVELOPMENT_MOSQUITTO_URL = "localhost:8080";

    private static final String PRODUCTION_SERVER_URL = "";
    private static final String PRODUCTION_MOSQUITTO_URL = "";

    private static boolean debug = true;

    public static String getServerURL() {
        return (debug) ? DEVELOPMENT_SERVER_URL : PRODUCTION_SERVER_URL;
    }

    public static String getMosquittoURL() {
        return (debug) ? DEVELOPMENT_MOSQUITTO_URL : PRODUCTION_MOSQUITTO_URL;
    }

}