package com.jaysyko.wrestlechat.services;

import android.os.Binder;

import com.jaysyko.wrestlechat.models.Message;

/**
 * Created by jarushaan on 2016-03-19
 */
public class ChatStreamBinder extends Binder {
    private ChatStream chatService;
    private IMessageArrivedListener mListener;

    public ChatStreamBinder(ChatStream chatService) {
        this.chatService = chatService;
    }

    public ChatStream getService() {
        return chatService;
    }

    public void setMessageArrivedListener(IMessageArrivedListener listener) {
        mListener = listener;
    }

    public void messageArrived(Message message) {
        if (mListener != null)
            mListener.messageArrived(message);
    }
}
