package com.jaysyko.wrestlechat.messageGenerator;

import android.view.View;

import com.jaysyko.wrestlechat.models.Message;

/**
 * MessageUIGenerator.java
 * Interface to generate views (UI Components)
 * @author Jay Syko
 */
public interface MessageGenerator {

    View generateView(MessagePosition position, Message message);
}
