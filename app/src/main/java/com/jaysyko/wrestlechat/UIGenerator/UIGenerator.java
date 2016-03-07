package com.jaysyko.wrestlechat.UIGenerator;

import android.view.View;

import com.jaysyko.wrestlechat.models.Message;

/**
 * MessageUIGenerator.java
 * Interface to generate views (UI Components)
 * @author Jay Syko
 */
public interface UIGenerator {

    View generateView(MessagingUIPosition position, Message message);
}
