package com.jaysyko.wrestlechat.network.requestData;

/**
 * Created by jarushaan on 2016-06-03
 */
public class AuthData {

    private String username;
    private String password;

    public AuthData(String username, String password){
        this.username = username;
        this.password = password;
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
