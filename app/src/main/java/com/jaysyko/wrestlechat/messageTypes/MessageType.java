package com.jaysyko.wrestlechat.messageTypes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.analytics.MessagingTracker;
import com.jaysyko.wrestlechat.customViews.AutoResizeTextView;
import com.jaysyko.wrestlechat.customViews.RoundedCornerImageView;
import com.jaysyko.wrestlechat.keys.Analytics;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;
import com.jaysyko.wrestlechat.utils.ImageTools;

public class MessageType implements MessageGenerator {

    private static final int TEXT_SIZE = 14, IMAGE_MSG_WIDTH = 1000, IMAGE_MSG_HEIGHT = 1000, IMAGE_MSG_PADDING_LEFT = 50, ZERO = 0;
    private static final String WHITE = "#FFFFFF";
    private static final String DEFAULT_SETTINGS_VALUE = "0";
    private static final String CHAT_BUBBLE_STYLE = "chatBubbleStyle";
    private Context context;
    private MessagePosition position;
    private Message message;

    public MessageType(Context context) {
        this.context = context;
    }

    /**
     * Returns a AutoResizeTextView for regular text messages
     * @return View
     */
    private AutoResizeTextView textMessage() {
        MessagingTracker.trackMessageType(Analytics.TEXT_MESSAGE);
        AutoResizeTextView textView = new AutoResizeTextView(this.context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        SharedPreferences settings = PreferenceProvider.getSharedPreferences(context, Preferences.SETTINGS);
        Integer bg = Integer.parseInt(settings.getString(CHAT_BUBBLE_STYLE, DEFAULT_SETTINGS_VALUE));
        switch (this.position) {
            case USER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_END);
                } else {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                textView.setBackgroundResource(R.drawable.bubble_white);
                break;
            case SENDER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_START);
                } else {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }
                lp.addRule(RelativeLayout.BELOW, R.id.senderUsernameID);
                int resID = R.drawable.bubble_left_brown;
                switch (bg) {
                    case 1:
                        resID = R.drawable.bubble_left_dark_green;
                        break;
                    case 2:
                        resID = R.drawable.bubble_left_red;
                        break;
                    case 3:
                        resID = R.drawable.bubble_left_black;
                }
                textView.setBackgroundResource(resID);
                textView.setTextColor(Color.parseColor(WHITE));
                break;
        }
        textView.setAutoLinkMask(Linkify.WEB_URLS);
        textView.setLinksClickable(true);
        textView.setGravity(Gravity.START);
        textView.setTextSize(TEXT_SIZE);
        textView.setLayoutParams(lp);
        textView.setId(R.id.textMessageID);
        textView.setText(this.message.getBody());
        return textView;
    }

    /**
     * Creates an ImageView Message if the user sends a link to a .jpg or .png
     * @return ImageView
     */
    private ImageView imageMessage() {
        MessagingTracker.trackMessageType(Analytics.IMAGE_MESSAGE);
        ImageView imgMsg = new RoundedCornerImageView(this.context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(IMAGE_MSG_WIDTH, IMAGE_MSG_HEIGHT);
        switch (this.position) {
            case USER:
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case SENDER:
                imgMsg.setPadding(IMAGE_MSG_PADDING_LEFT, ZERO, ZERO, ZERO);
                lp.addRule(RelativeLayout.BELOW, R.id.senderUsernameID);
                break;
        }
        imgMsg.setLayoutParams(lp);
        imgMsg.setId(R.id.imageMessageID);
        ImageTools.loadImage(this.context, this.message.getBody(), imgMsg);
        return imgMsg;
    }

    /**
     * @param position MessagingUIPosition
     * @param message Message
     * @return View
     */
    public View generateView(MessagePosition position, Message message) {
        this.position = position;
        this.message = message;
        if (ImageTools.isLinkToImage(this.message.getBody())) {
            return imageMessage();
        }
        return textMessage();
    }
}
