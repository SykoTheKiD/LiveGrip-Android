package com.jaysyko.wrestlechat.services;

import com.jaysyko.wrestlechat.models.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarushaan on 2016-03-10
 */
public class MessageListWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Message> messages = new ArrayList<>();

    public MessageListWrapper(List<Message> messageList) {
        this.messages.clear();
        this.messages.addAll(messageList);
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

}
