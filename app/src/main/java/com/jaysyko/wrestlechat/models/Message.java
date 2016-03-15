package com.jaysyko.wrestlechat.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Message.java
 * Model for a message in the database
 *
 * @author Jay Syko
 */

public class Message {

    public static final String USERNAME = "username";
    public static final String EVENT_ID = "eventId";
    public static final String MSG_BODY = "body";
    public static final String CREATED_AT = "createdAt";

    /**
     * Returns the owner of the message
     *
     * @return username
     */
    public String getUserID() {
        return getString(USERNAME);
    }

    /**
     * Set the user's username associated to the message
     *
     * @param username String
     */
    public void setUserID(String username) {
        put(USERNAME, username);
    }

    /**
     * Set the event the message was sent to
     *
     * @param eventId String
     */
    public void setEventId(String eventId) {
        put(EVENT_ID, eventId);
    }

    /**
     * Returns the message contents
     *
     * @return body String
     */
    public String getBody() {
        return getString(MSG_BODY);
    }

    /**
     * Assigns the message some content
     *
     * @param body String
     */
    public void setBody(String body) {
        put(MSG_BODY, body);
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return getUserID();
    }

}