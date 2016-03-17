package com.jaysyko.wrestlechat.models;

import java.io.Serializable;

/**
 * Message.java
 * Model for a message in the database
 *
 * @author Jay Syko
 */

public class Message implements Serializable {

    private String userID, eventID, body;

    public Message(String userID, String eventID, String body) {
        this.userID = userID;
        this.eventID = eventID;
        this.body = body;
    }

    /**
     * Returns the owner of the message
     *
     * @return username
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * @return eventID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Returns the message contents
     *
     * @return body String
     */
    public String getBody() {
        return this.body;
    }

}