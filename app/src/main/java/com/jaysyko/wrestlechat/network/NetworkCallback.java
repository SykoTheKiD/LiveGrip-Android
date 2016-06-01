package com.jaysyko.wrestlechat.network;

/**
 * NetworkCallback.java
 *
 * Callback for when a request to a URL was successful
 * @author Jay Syko
 */
public interface NetworkCallback {
    void onSuccess(String response);

    void onFail(String response);
}

