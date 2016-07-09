package com.jaysyko.wrestlechat.network.requestData;

/**
 * Created by jarushaan on 2016-06-18
 */
public class MessageData {

    private int event_id, user_id;
    private String body, username, profile_image;

    public MessageData(int eventID, int userID, String body, String username, String profileImage) {
        this.event_id = eventID;
        this.user_id = userID;
        this.body = body;
        this.username = username;
        this.profile_image = profileImage;
    }

    public int getEventID() {
        return this.event_id;
    }

    public int getUserID() {
        return this.user_id;
    }

    public String getBody() {
        return this.body;
    }

    public String getUsername() {
        return this.username;
    }

    public String getProfileImage() {
        return this.profile_image;
    }
}
