package com.jaysyko.wrestlechat.services;

/**
 * @author Jay Syko on 2016-07-24.
 */
public interface ServiceProvider {

    MessagingService getMessagingService();
    MessagingServiceBinder getMessagingServiceBinder();
}
