package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;

import com.jaysyko.wrestlechat.models.Event;

/**
 * @author Jay Syko on 2016-07-12.
 */
public interface NotifyStore {

    void add(Event event, Context context);
    void remove(Event event, Context context);
    boolean contains(Event event);

}
