package com.jaysyko.wrestlechat.messageTypes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.analytics.MessagingTracker;
import com.jaysyko.wrestlechat.customViews.RoundedCornerImageView;
import com.jaysyko.wrestlechat.keys.Analytics;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceKeys;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;
import com.jaysyko.wrestlechat.utils.ImageTools;

public class MessageType implements MessageGenerator {

    private static final int TEXT_SIZE = 14, IMAGE_MSG_WIDTH = 1000, IMAGE_MSG_HEIGHT = 1000, IMAGE_MSG_PADDING_LEFT = 50, ZERO = 0;
    private static final String WHITE = "#FFFFFF";
    private static final int DEFAULT_SETTINGS_VALUE = R.color.colorAccent;
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
    private TextView textMessage() {
        MessagingTracker.trackMessageType(Analytics.TEXT_MESSAGE);
        TextView textView = new TextView(this.context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        SharedPreferences settings = PreferenceProvider.getSharedPreferences(context, Preferences.SETTINGS);
        int bg = settings.getInt(PreferenceKeys.MESSAGING_BUBBLE_COLOUR, DEFAULT_SETTINGS_VALUE);
        switch (this.position) {
            case USER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_END);
                }else{
                    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    final Drawable myIcon = context.getResources().getDrawable( R.drawable.bubble_right, context.getTheme());
                    final ColorFilter filter = new LightingColorFilter(bg, bg);
                    if(myIcon != null){
                        myIcon.setColorFilter(filter);
                    }
                    textView.setBackground(myIcon);
                }else{
                    final Drawable myIcon = context.getResources().getDrawable(R.drawable.bubble_right);
                    final ColorFilter filter = new LightingColorFilter(bg, bg);
                    if(myIcon != null){
                        myIcon.setColorFilter(filter);
                    }
                    textView.setBackgroundDrawable(myIcon);
                }
                textView.setPadding(25,15,80,15);
                break;
            case SENDER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.addRule(RelativeLayout.ALIGN_PARENT_START);
                }else{
                    lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    final Drawable myIcon = context.getResources().getDrawable(R.drawable.bubble_left, context.getTheme());
                    final ColorFilter filter = new LightingColorFilter(bg, bg);
                    textView.setBackground(myIcon);
                    if(myIcon != null){
                        myIcon.setColorFilter(filter);
                    }
                }else{
                    final Drawable myIcon = context.getResources().getDrawable(R.drawable.bubble_right);
                    final ColorFilter filter = new LightingColorFilter(bg, bg);
                    if(myIcon != null){
                        myIcon.setColorFilter(filter);
                    }
                    textView.setBackgroundDrawable(myIcon);
                }
                lp.addRule(RelativeLayout.BELOW, R.id.senderUsernameID);
                textView.setPadding(80,15,25,15);
                break;
        }
        textView.setTextColor(Color.parseColor(WHITE));
        textView.setLayoutParams(lp);
        textView.setAutoLinkMask(Linkify.WEB_URLS);
        textView.setLinksClickable(true);
        textView.setGravity(Gravity.START);
        textView.setTextSize(TEXT_SIZE);
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
