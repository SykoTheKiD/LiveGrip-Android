package com.jaysyko.wrestlechat.UIGenerator;

import android.view.View;

import com.jaysyko.wrestlechat.models.Message;

/**
 * MessageUIGenerator.java
 */
public interface UIGenerator {

    View generateView(MessagingUIPosition position, Message message);
}
