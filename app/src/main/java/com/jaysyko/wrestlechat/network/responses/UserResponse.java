package com.jaysyko.wrestlechat.network.responses;

import com.google.gson.annotations.SerializedName;
import com.jaysyko.wrestlechat.models.User;

/**
 * Created by jarushaan on 2016-06-03
 */
public class UserResponse {

    @SerializedName(JSONKeys.STATUS)
    private String status;
    @SerializedName(JSONKeys.MESSAGE)
    private String message;
    @SerializedName(JSONKeys.PAYLOAD)
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

