package com.jaysyko.wrestlechat.UIGenerator;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.jaysyko.wrestlechat.customImageView.RoundedImageView;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.utils.ImageTools;

/**
 *
 */
public class MessagingUI implements UIGenerator {

    private Context context;
    private MessagingUIPosition position;
    private Message message;

    public MessagingUI(Context context) {
        this.context = context;
    }

    private RelativeLayout generateContainer() {
        return null;
    }

    private RoundedImageView profileImage() {
        switch (position) {
            case USER:
                break;
            case SENDER:
                break;
        }
        RoundedImageView profileImageView = new RoundedImageView(this.context, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(65, 65);
        profileImageView.setLayoutParams(lp);
        ImageTools.loadImage(this.context, message.getUserImage(), profileImageView);
        return profileImageView;
    }

    @Override
    public View generateView(MessagingUIPosition position, Message message) {
        this.position = position;
        this.message = message;
        profileImage();
        generateContainer();
        return null;
    }
}
