package com.jaysyko.wrestlechat.models;

import com.google.gson.annotations.SerializedName;
import com.jaysyko.wrestlechat.application.eLog;

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
    private static final String POPULARITY = "rating";
    private static final String SEGMENT_NAME = "segment_name";
    private static final String ADDITIONAL_DETAIL = "additional_detail";

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
    private boolean notify = false;

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

    public List<EventCard> getMatchList() {
        List<EventCard> ret = new ArrayList<>();
        String matchCard = getMatchCard();
        try {
            JSONObject matches = new JSONObject(matchCard);
            JSONArray matchesArray = (JSONArray) matches.get(Utils.EVENT_DETAILS);
            JSONObject match;
            for (int i = 0; i < matchesArray.length(); i++) {
                match = matchesArray.getJSONObject(i);
                try {
                    ret.add(new EventCard(match.getString(SEGMENT_NAME), match.getString(ADDITIONAL_DETAIL), match.getInt(POPULARITY)));
                } catch (JSONException e) {
                    eLog.e(TAG, e.getMessage());
                    ret.add(new EventCard(match.getString(SEGMENT_NAME), match.getInt(POPULARITY)));
                }
            }

        } catch (JSONException e) {
            eLog.e(TAG, e.getMessage());
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

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @Override
    public String toString(){
        return this.eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return eventID == event.eventID;

    }

    @Override
    public int hashCode() {
        return eventID;
    }

    public class EventCard {
        private String segment, additionalDetail;
        private Integer rating;

        EventCard(String segment, String additionalDetail, int rating) {
            this.segment = segment;
            this.additionalDetail = additionalDetail;
            this.rating = rating;
        }

        EventCard(String segment, int rating) {
            this.segment = segment;
            this.rating = rating;
        }

        public String getSegment() {
            return segment;
        }

        public String getAdditionalDetail() {
            return additionalDetail;
        }

        public Integer getRating(){
            return rating;
        }

    }
}