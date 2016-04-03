package com.jaysyko.wrestlechat.models;

import com.jaysyko.wrestlechat.utils.ImageTools;

import java.io.Serializable;

/**
 * Message.java
 * Model for a message in the database
 *
 * @author Jay Syko
 */

public class Message implements Serializable {

    private String username;
    private String body;
    private String userImage;

    public Message(String username, String body, String userImage) {
        this.username = username;
        if (userImage == null) {
            this.userImage = ImageTools.defaultProfileImage(username);
        } else {
            this.userImage = userImage;
        }
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

    @Override
    public String toString() {
        return this.body;
    }

    /**
     * @author Jay Syko
     */
    public enum MessageJSONKeys {
        USERNAME("username"), BODY("body"), PROFILE_IMAGE("profile_image"), EVENT_ID("id"), USER_ID("id");
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