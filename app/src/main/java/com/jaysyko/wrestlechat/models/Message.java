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
    private int eventID;
    private int userID;

    public Message(String username, String body, String userImage, int eventID, int userID) {
        this.username = username;
        if (userImage == null) {
            this.userImage = ImageTools.defaultProfileImage(username);
        } else {
            this.userImage = userImage;
        }
        this.eventID = eventID;
        this.userID = userID;
        this.body = body;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

}