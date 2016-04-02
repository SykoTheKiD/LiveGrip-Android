package com.jaysyko.wrestlechat.services;

import com.jaysyko.wrestlechat.models.Message;

/**
 * Interface for a message received callback
 * @author Jay Syko
 */
public interface IMessageArrivedListener {

    void messageArrived(Message message);
}
