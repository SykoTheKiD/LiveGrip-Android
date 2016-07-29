package com.jaysyko.wrestlechat.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Jay Syko on 2016-07-23.
 */
public class Token {

    @SerializedName(Utils.TOKEN_KEY)
    private String key;
    @SerializedName(Utils.EXPIRY_DATE)
    private String expiry_date;

    public String getExpiryDate() {
        return expiry_date;
    }

    public void setExpiryDate(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getToken() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
