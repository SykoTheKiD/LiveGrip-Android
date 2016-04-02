package com.jaysyko.wrestlechat.models;

import java.io.Serializable;

/**
 * Message.java
 * Model for a message in the database
 *
 * @author Jay Syko
 */

public class Message implements Serializable {

    private String username;
    private String eventName;
    private String body;
    private String userImage;

    public Message(String username, String eventName, String body, String userImage) {
        this.username = username;
        this.eventName = eventName;
        this.userImage = userImage;
        this.body = body;
    }

    /**
     * Returns the owner of the message
     *
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return eventID
     */
    public String getEventName() {
        return this.eventName;
    }

    /**
     * @return userImage
     */
    public String getUserImage() {
        return this.userImage;
    }


    /**
     * Returns the message contents
     *
     * @return body String
     */
    public String getBody() {
        return this.body;
    }

    /**
     * Created by jarushaan on 2016-03-19
     */
    public enum MessageJSONKeys {
        USERNAME("username"), EVENT_NAME("name"), BODY("body"), PROFILE_IMAGE("profile_image"), EVENT_ID("event_id"), USER_ID("user_id");
        private String key;

        MessageJSONKeys(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return this.key;
        }
    }
}