package com.jaysyko.wrestlechat.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Events.java
 * Model for the Event object in the database
 * Contains all the keys in the database table
 *
 * @author Jay Syko
 */

public class Event{

    private static final String TAG = Event.class.getSimpleName();

    @SerializedName(Utils.ID)
    private int eventID;
    @SerializedName(Utils.NAME)
    private String eventName;
    @SerializedName(Utils.INFO)
    private String eventInfo;
    @SerializedName(Utils.MATCH_CARD)
    private String matchCard;
    @SerializedName(Utils.IMAGE)
    private String eventImage;
    @SerializedName(Utils.LOCATION)
    private String eventLocation;
    @SerializedName(Utils.START_TIME)
    private String eventStartTime;
    @SerializedName(Utils.END_TIME)
    private String eventEndTime;

    public Event(int eventID, String eventName, String eventInfo, String matchCard, String eventImage, String eventLocation, String eventStartTime, String eventEndTime) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventInfo = eventInfo;
        this.matchCard = matchCard;
        this.eventImage = eventImage;
        this.eventLocation = eventLocation;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }

    /**
     * Returns the current event's EVENT_ID_KEY
     *
     * @return String
     */
    public int getEventID() {
        return this.eventID;
    }

    /**
     * Returns the current event's name
     *
     * @return String
     */
    public String getEventName() {
        return this.eventName;
    }

    /**
     * Returns the current event's information
     *
     * @return String
     */
    public String getEventInfo() {
        return this.eventInfo;
    }

    /**
     * Return the match card for the current event
     *
     * @return String
     */
    public String getMatchCard() {
        return this.matchCard;
    }

    /**
     * Return a list of matches
     *
     * @return List
     */

    public List<String> getMatchList() {
        List<String> ret = new ArrayList<>();
        String matchCard = getMatchCard();
        try {
            JSONObject matches = new JSONObject(matchCard);
            JSONArray matchesArray = (JSONArray) matches.get(Utils.EVENT_DETAILS);
            for (int i = 0; i < matchesArray.length(); i++) {
                ret.add(matchesArray.getString(i));
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return ret;
    }

    /**
     * Returns the event's image URL
     *
     * @return String
     */
    public String getEventImage() {
        return this.eventImage;
    }

    /**
     * Returns the event's location
     *
     * @return String
     */
    public String getEventLocation() {
        return this.eventLocation;
    }

    /**
     * Returns the event's startTime
     *
     * @return Long
     */
    public String getEventStartTime() {
        return this.eventStartTime;
    }

    /**
     * Return the event's endTime
     * @return Long
     */
    public String getEventEndTime() {
        return this.eventEndTime;
    }

    @Override
    public String toString(){
        return this.eventName;
    }

}