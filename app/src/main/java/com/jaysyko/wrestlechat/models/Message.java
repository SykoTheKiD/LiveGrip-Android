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

@ParseClassName(Message.PARSE_MESSAGE_TABLE)
public class Message extends ParseObject implements Serializable {

    public static final String PARSE_MESSAGE_TABLE = "Message";
    public static final String USERNAME = "username";
    public static final String EVENT_ID = "eventId";
    public static final String MSG_BODY = "body";
    public static final String CREATED_AT = "createdAt";
    public static final String USER_IMAGE = "user_image";

    /**
     *
     * @return image_url
     */
    public String getUserImage() {
        return getString(USER_IMAGE);
    }

    /**
     * @param user_image String
     */
    public void setUserImage(String user_image) {
        put(USER_IMAGE, user_image);
    }

    /**
     * Returns the owner of the message
     *
     * @return username
     */
    public String getUsername() {
        return getString(USERNAME);
    }

    /**
     * Set the user's username associated to the message
     *
     * @param username String
     */
    public void setUsername(String username) {
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

    public String toString() {
        return getUsername();
    }
}