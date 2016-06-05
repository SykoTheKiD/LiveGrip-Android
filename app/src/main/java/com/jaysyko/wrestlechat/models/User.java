package com.jaysyko.wrestlechat.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jarushaan on 2016-06-03
 */
public class User {

    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("profile_image")
    private String profile_image;

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
