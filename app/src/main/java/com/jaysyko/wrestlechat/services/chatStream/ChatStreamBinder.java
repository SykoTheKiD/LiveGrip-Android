package com.jaysyko.wrestlechat.services.chatStream;

import android.os.Binder;

/**
 * Created by jarushaan on 2016-03-19
 */
public class ChatStreamBinder extends Binder {

    private ChatStream chatService;

    public ChatStreamBinder(ChatStream chatService) {
        this.chatService = chatService;
    }

    public ChatStream getService() {
        return this.chatService;
    }
}
