package com.jaysyko.wrestlechat.network;

import com.android.volley.Request;

/**
 * Created by jarushaan on 2016-03-23
 */
public enum RequestTypes {
    GET(Request.Method.GET), POST(Request.Method.POST), DELETE(Request.Method.DELETE), PUT(Request.Method.PUT);

    private int requestType;

    RequestTypes(int type) {
        this.requestType = type;
    }

    public int getRequestType() {
        return this.requestType;
    }
}
