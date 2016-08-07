package com.jaysyko.wrestlechat.network.requestData;

import com.jaysyko.wrestlechat.application.App;

/**
 * Created by jarushaan on 2016-06-03
 */
public class AuthData {

    private String username;
    private String password;
    private String app_version;
    private String profile_image;

    public AuthData(String username, String password){
        this.username = username;
        this.password = password;
        this.app_version = App.APP_VERSION;
    }

    public AuthData(String username, String password, String profile_image){
        this.username = username;
        this.password = password;
        this.profile_image = profile_image;
        this.app_version = App.APP_VERSION;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
