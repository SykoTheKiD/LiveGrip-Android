package com.jaysyko.wrestlechat.UIGenerator;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.customTextView.AutoResizeTextView;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.utils.ImageTools;

public class MessagingUIComponents implements UIGenerator {

    private static final int TEXT_SIZE = 14;
    private Context context;
    private MessagingUIPosition position;
    private Message message;

    public MessagingUIComponents(Context context) {
        this.context = context;
    }

    /**
     * @return View
     */
    private AutoResizeTextView textMessage() {
        AutoResizeTextView textView = new AutoResizeTextView(this.context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                lp.addRule(RelativeLayout.BELOW, R.id.sender_username);
                textView.setBackgroundResource(R.drawable.bubble_left_brown);
                break;
        }
        textView.setGravity(Gravity.START);
        textView.setTextSize(TEXT_SIZE);
        textView.setLayoutParams(lp);
        textView.setId(R.id.textMessageID);
        textView.setText(this.message.getBody());
        return textView;
    }

    /**
     * @return ImageView
     */
    private ImageView imageMessage() {
        ImageView imgMsg = new ImageView(this.context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(250, 250);
        switch (this.position) {
            case USER:
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case SENDER:
                imgMsg.setPadding(5, 0, 0, 0);
                break;
        }
        imgMsg.setLayoutParams(lp);
        imgMsg.setId(R.id.imageMessageID);
        ImageTools.loadImage(this.context, this.message.getBody(), imgMsg);
        return imgMsg;
    }

    public View generateView(MessagingUIPosition position, Message message) {
        this.position = position;
        this.message = message;
        if (ImageTools.isLinkToImage(this.message.getBody())) {
            return imageMessage();
        }
        return textMessage();
    }
}
