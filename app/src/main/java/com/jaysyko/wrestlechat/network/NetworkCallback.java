package com.jaysyko.wrestlechat.network;

/**
 * NetworkCallback.java
 *
 * Callback for when a request to a URL was successful
 * @author Jay Syko
 */
public interface NetworkCallback<T> {
    void onSuccess(T response);

    void onFail(String t);
}

