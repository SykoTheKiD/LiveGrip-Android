package com.jaysyko.wrestlechat.network;

import com.jaysyko.wrestlechat.network.responses.BadRequestResponse;

/**
 * NetworkCallback.java
 *
 * Callback for when a request to a URL was successful
 * @author Jay Syko
 */
public interface NetworkCallback<T> {
    void onSuccess(T response);

    void onFail(BadRequestResponse error);
}

