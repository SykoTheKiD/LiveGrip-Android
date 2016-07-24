package com.jaysyko.wrestlechat.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jay Syko on 2016-07-23.
 */
public class Token {

    @SerializedName(Utils.TOKEN)
    private String token;
    @SerializedName(Utils.EXPIRY_DATE)
    private String expiry_date;

    public String getExpiryDate() {
        return expiry_date;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiry_date = expiryDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
