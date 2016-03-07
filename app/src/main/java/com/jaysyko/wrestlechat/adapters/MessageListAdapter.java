package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.UIGenerator.MessagingUI;
import com.jaysyko.wrestlechat.UIGenerator.MessagingUIComponents;
import com.jaysyko.wrestlechat.UIGenerator.MessagingUIPosition;
import com.jaysyko.wrestlechat.UIGenerator.UIGenerator;
import com.jaysyko.wrestlechat.adapters.viewholders.MessageViewHolder;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.utils.ImageTools;

import java.util.List;

/**
 * MessageListAdapter.java
 * Adapter to hold all messages in a conversation
 *
 * @author Jay Syko
 */
public class MessageListAdapter extends ArrayAdapter<Message> {
    private Handler handler = new Handler();
    private String mUserId;
    private Context context = getContext();
    private final UIGenerator uiComponents = new MessagingUIComponents(context);
    private final UIGenerator messagingUI = new MessagingUI(context);
    public MessageListAdapter(Context context, String userId, List<Message> messages) {
        super(context, 0, messages);
        this.mUserId = userId;
    }

    /**
     * Gets the current view the adapter is working under
     * @param position int
     * @param convertView View
     * @param parent ViewGroup
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(parent);
        }
        final Message message = getItem(position);
        final MessageViewHolder holder = (MessageViewHolder) convertView.getTag();
        final boolean isMe = message.getUsername().equals(mUserId);
        handler.post(new Runnable() {
            @Override
            public void run() {
                final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
                ImageTools.loadImage(context, message.getUserImage(), profileView);
            }
        });
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    setUserView(holder, message);
                }
            });
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    senderView(holder, message);
                }
            });
        }
        return convertView;
    }

    /**
     * Inflates the current view
     * @param parent ViewGroup
     * @return View
     */
    @NonNull
    private View getView(ViewGroup parent) {
        View convertView;
        convertView = LayoutInflater.from(context).
                inflate(R.layout.message_bubble, parent, false);
        final MessageViewHolder holder = new MessageViewHolder();
        holder.imageLeft = (ImageView) convertView.findViewById(R.id.ivProfileLeft);
        holder.imageRight = (ImageView) convertView.findViewById(R.id.ivProfileRight);
        holder.sender = (RelativeLayout) convertView.findViewById(R.id.sender_message);
        holder.user = (RelativeLayout) convertView.findViewById(R.id.my_message);
        convertView.setTag(holder);
        return convertView;
    }

    /**
     *
     *
     * Constructs the sender's side of the message
     * @param message Message
     * @param holder MessageViewHolder
     */
    private void senderView(MessageViewHolder holder, Message message) {
        RelativeLayout senderContainer = holder.sender;
        holder.imageLeft.setVisibility(View.VISIBLE);
        holder.sender.setVisibility(View.VISIBLE);

        holder.imageRight.setVisibility(View.GONE);
        holder.user.setVisibility(View.GONE);
        TextView usernameTV = generateUsername();
//        TextView usernameTV = (TextView) senderContainer.findViewById(R.id.sender_username);
        usernameTV.setText(message.getUsername());
        View view = uiComponents.generateView(MessagingUIPosition.SENDER, message);
        senderContainer.removeAllViews();
        senderContainer.addView(usernameTV);
        senderContainer.addView(view);
    }

    /**
     * Constructs the user's side of the message
     * @param holder MessageViewHolder
     * @param message String
     */
    private void setUserView(MessageViewHolder holder, Message message) {
        View view = uiComponents.generateView(MessagingUIPosition.USER, message);
        RelativeLayout userContainer = holder.user;
        userContainer.removeAllViews();
        holder.imageRight.setVisibility(View.VISIBLE);
        holder.user.setVisibility(View.VISIBLE);

        holder.imageLeft.setVisibility(View.GONE);
        holder.sender.setVisibility(View.GONE);

        userContainer.addView(view);
    }

    /**
     * @return
     */
    private TextView generateUsername() {
        TextView username = new TextView(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_START);
        } else {
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        username.setLayoutParams(lp);
        username.setGravity(Gravity.START);
        username.setMaxLines(1);
        username.setTextSize(12);
        username.setTypeface(null, Typeface.BOLD);
        username.setId(R.id.senderUsernameID);
        username.setPadding(50, 0, 0, 0);
        return username;
    }

}