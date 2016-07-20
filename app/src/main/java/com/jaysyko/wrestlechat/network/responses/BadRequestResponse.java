package com.jaysyko.wrestlechat.network.responses;

/**
 * @author Jay Syko on 2016-07-20.
 */
public class BadRequestResponse {

    private int code;
    private String message;

    public BadRequestResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
