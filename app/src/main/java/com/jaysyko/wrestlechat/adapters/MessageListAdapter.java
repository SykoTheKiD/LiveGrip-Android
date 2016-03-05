package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.viewholders.MessageViewHolder;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.customTextView.AutoResizeTextView;
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
     * Constructs the sender's side of the message
     * @param message Message
     * @param holder MessageViewHolder
     */
    private void senderView(MessageViewHolder holder, Message message) {
        TextView usernameTV;
        AutoResizeTextView messageBodyTV;
        holder.imageLeft.setVisibility(View.VISIBLE);
        holder.imageRight.setVisibility(View.GONE);
        holder.user.setVisibility(View.GONE);
        holder.sender.setVisibility(View.VISIBLE);
        messageBodyTV = (AutoResizeTextView) holder.sender.findViewById(R.id.sender_message_body);
        messageBodyTV.setText(message.getBody());
        usernameTV = (TextView) holder.sender.findViewById(R.id.sender_username);
        usernameTV.setText(message.getUsername());
    }

    /**
     * Constructs the user's side of the message
     * @param holder MessageViewHolder
     * @param message String
     */
    private void setUserView(MessageViewHolder holder, Message message) {
        AutoResizeTextView messageBodyTV;
        holder.imageRight.setVisibility(View.VISIBLE);
        holder.imageLeft.setVisibility(View.GONE);
        holder.sender.setVisibility(View.GONE);
        holder.user.setVisibility(View.VISIBLE);
        messageBodyTV = (AutoResizeTextView) holder.user.findViewById(R.id.my_message_body);
        messageBodyTV.setText(message.getBody());
    }
}