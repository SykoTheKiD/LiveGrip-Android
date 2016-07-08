package com.jaysyko.wrestlechat.network.requestData;

/**
 * Created by jarushaan on 2016-06-18
 */
public class MessageData {

    private int eventID, userID;
    private String body, username, profileImage;

    public MessageData(int eventID, int userID, String body, String username, String profileImage) {
        this.eventID = eventID;
        this.userID = userID;
        this.body = body;
        this.username = username;
        this.profileImage = profileImage;
    }

    public int getEventID() {
        return this.eventID;
    }

    public int getUserID() {
        return this.userID;
    }

    public String getBody() {
        return this.body;
    }

    public String getUsername() {
        return this.username;
    }

    public String getProfileImage() {
        return this.profileImage;
    }
}
