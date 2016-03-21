package com.jaysyko.wrestlechat.services;

import com.jaysyko.wrestlechat.models.Message;

/**
 * Created by jarushaan on 2016-03-20
 */
public interface IMessageArrivedListener {

    void messageArrived(Message message);
}
