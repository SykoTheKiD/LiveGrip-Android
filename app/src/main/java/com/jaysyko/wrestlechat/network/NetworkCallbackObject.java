package com.jaysyko.wrestlechat.network;

import java.util.List;

public interface NetworkCallbackObject<T> {
    void onSuccess(List<T> response);
}
