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

    @SerializedName(Utils.USER_ID)
    private int userID;
    @SerializedName(Utils.USERNAME)
    private String username;
    @SerializedName(Utils.PROFILE_IMAGE)
    private String profileImage;
    @SerializedName(Utils.EVENT_ID)
    private int eventID;
    @SerializedName(Utils.MESSAGE_ID)
    private int messageID;
    @SerializedName(Utils.MESSAGE_BODY)
    private String body;
    @SerializedName(Utils.ID)
    private String id;

    public Message(int userId, String username, String profileImage, int eventId, int messageId, String body, String id) {
        this.userID = userId;
        this.username = username;
        this.profileImage = profileImage == null ? ImageTools.defaultProfileImage(username) : profileImage;
        this.eventID = eventId;
        this.messageID = messageId;
        this.body = body;
        this.id = id;
    }

    public Message(String username, String profileImage, String body) {
        this.username = username;
        this.profileImage = profileImage;
        this.body = body;
    }

    private String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}