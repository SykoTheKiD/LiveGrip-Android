package com.jaysyko.wrestlechat.adapters.viewholders;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaysyko.wrestlechat.customTextView.AutoResizeTextView;

/**
 * MessageViewHolder.java
 * ViewHolder for the messages
 *
 * @author Jay Syko
 */
public final class MessageViewHolder {
    public ImageView imageLeft, imageRight;
    public AutoResizeTextView senderMessage;
    public RelativeLayout sender, user;
}
