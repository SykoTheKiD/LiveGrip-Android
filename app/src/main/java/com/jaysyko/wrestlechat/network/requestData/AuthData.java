package com.jaysyko.wrestlechat.network.requestData;

import com.jaysyko.wrestlechat.application.App;

/**
 * Created by jarushaan on 2016-06-03
 */
public class AuthData {

    private String username;
    private String password;
    private String app_version;

    public AuthData(String username, String password){
        this.username = username;
        this.password = password;
        this.app_version = App.APP_VERSION;
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
