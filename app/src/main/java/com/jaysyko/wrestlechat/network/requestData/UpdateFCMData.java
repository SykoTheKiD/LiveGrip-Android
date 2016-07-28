package com.jaysyko.wrestlechat.network.requestData;

/**
 * @author Jay Syko on 2016-07-27.
 */
public class UpdateFCMData {

    private int user_id;
    private String access_token;

    public UpdateFCMData(int user_id, String access_token){
        this.user_id = user_id;
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
