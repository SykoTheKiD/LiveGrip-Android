package com.jaysyko.wrestlechat.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jarushaan on 2016-06-03
 */
public class User {

    @SerializedName(Utils.ID)
    private int id;
    @SerializedName(Utils.USERNAME)
    private String username;
    @SerializedName(Utils.PROFILE_IMAGE)
    private String profile_image;
    @SerializedName(Utils.TOKEN)
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getProfileImage() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
