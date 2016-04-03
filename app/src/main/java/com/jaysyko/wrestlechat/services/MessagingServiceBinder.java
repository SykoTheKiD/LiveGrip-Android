package com.jaysyko.wrestlechat.services;

import android.os.Binder;
import android.util.Log;

import com.jaysyko.wrestlechat.models.Message;

/**
 * Public Interface for MessagingService.java
 *
 * @author Jay Syko
 */
public class MessagingServiceBinder extends Binder {
    private MessagingService chatService;
    private IMessageArrivedListener mListener;

    public MessagingServiceBinder(MessagingService chatService) {
        this.chatService = chatService;
    }

    /**
     * Get an instance of MessagingService
     *
     * @return MessagingService instance
     */
    public MessagingService getService() {
        return chatService;
    }

    /**
     * Set a listener listening for incoming MQTT messages
     *
     * @param listener IMessageArrivedListener
     */
    public void setMessageArrivedListener(IMessageArrivedListener listener) {
        mListener = listener;
    }

    /**
     * Callback for when a message is received by a listener
     *
     * @param message Message
     */
    public void messageArrived(Message message) {
        Log.e("T2", message.toString());
        if (mListener != null)
            mListener.messageArrived(message);
    }
}
