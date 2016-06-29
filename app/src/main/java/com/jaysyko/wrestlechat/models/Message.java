package com.jaysyko.wrestlechat.models;

import com.google.gson.annotations.SerializedName;
import com.jaysyko.wrestlechat.utils.ImageTools;

import java.io.Serializable;

/**
 * Message.java
 * Model for a message in the database
 *
 * @author Jay Syko
 */

public class Message implements Serializable {

    @SerializedName(Utils.MESSAGE_USER)
    private MessageUser messageUser;
    @SerializedName(Utils.MESSAGE_BODY)
    private String body;

    public Message(String username, String body, String userImage) {
        if (userImage == null) {
            userImage = ImageTools.defaultProfileImage(username);
        }
        this.messageUser = new MessageUser(username, userImage);
        this.body = body;
    }

    /**
     * Returns the owner of the message
     *
     * @return username
     */
    public String getUsername() {
        return this.messageUser.getMessageUsername();
    }

    /**
     * @return userImage
     */
    public String getUserImage() {
        return this.messageUser.getMessageUserImage();
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

    private class MessageUser {
        @SerializedName(Utils.USERNAME)
        private String messageUsername;
        @SerializedName(Utils.PROFILE_IMAGE)
        private String messageUserImage;

        public MessageUser(String username, String profileImage) {
            setMessageUsername(username);
            setMessageUserImage(profileImage);
        }

        public String getMessageUserImage() {
            return messageUserImage;
        }

        public void setMessageUserImage(String messageUserImage) {
            this.messageUserImage = messageUserImage;
        }

        public String getMessageUsername() {
            return messageUsername;
        }

        public void setMessageUsername(String messageUsername) {
            this.messageUsername = messageUsername;
        }
    }

}