package com.jaysyko.wrestlechat.network.requestData;

/**
 * Created by jarushaan on 2016-06-18
 */
public class MessageData {

    private int eventID;
    private int userID;
    private String body;

    public MessageData(int eventID, int userID, String body){
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
