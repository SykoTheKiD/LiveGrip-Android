package com.jaysyko.wrestlechat.services;

import android.os.Binder;

/**
 * Created by jarushaan on 2016-03-10
 */
public class LocalMessageBinder extends Binder {
    private MessagingService messagingService;

    public LocalMessageBinder(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    public MessagingService getService() {
        return messagingService;
    }
}
