package com.jaysyko.wrestlechat.network;

import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;

/**
 * NetworkCallback.java
 *
 * Callback for when a request to a URL was successful
 * @author Jay Syko
 */
public interface NetworkCallback<T> {
    void onSuccess(T response);
    void onFail(FailedRequestResponse error);
}

