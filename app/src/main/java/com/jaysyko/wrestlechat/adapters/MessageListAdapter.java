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
import com.jaysyko.wrestlechat.textview.AutoResizeTextView;
import com.jaysyko.wrestlechat.utils.ImageTools;

import java.util.List;

public class MessageListAdapter extends ArrayAdapter<Message> {
    Handler handler1 = new Handler();
    Handler handler2 = new Handler();
    Handler handler3 = new Handler();
    private String mUserId;
    private Context context = getContext();
    public MessageListAdapter(Context context, String userId, List<Message> messages) {
        super(context, 0, messages);
        this.mUserId = userId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(parent);
        }
        final Message message = getItem(position);
        final MessageViewHolder holder = (MessageViewHolder) convertView.getTag();
        final boolean isMe = message.getUsername().equals(mUserId);
        final String messageBody = message.getBody();
        handler1.post(new Runnable() {
            @Override
            public void run() {
                final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
                ImageTools.loadImage(context, ImageTools.defaultProfileImage(message.getUsername()), profileView);
            }
        });
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            handler2.post(new Runnable() {
                @Override
                public void run() {
                    setUserView(holder, messageBody);
                }
            });
        } else {
            handler3.post(new Runnable() {
                @Override
                public void run() {
                    senderView(message, holder, messageBody);
                }
            });
        }
        return convertView;
    }

    @NonNull
    private View getView(ViewGroup parent) {
        View convertView;
        convertView = LayoutInflater.from(context).
                inflate(R.layout.message_bubble, parent, false);
        final MessageViewHolder holder = new MessageViewHolder();
        holder.imageLeft = (ImageView) convertView.findViewById(R.id.ivProfileLeft);
        holder.imageRight = (ImageView) convertView.findViewById(R.id.ivProfileRight);
        holder.sender = (RelativeLayout) convertView.findViewById(R.id.sender_message);
        holder.senderMessage = (TextView) convertView.findViewById(R.id.sender_message_body);
        holder.user = (RelativeLayout) convertView.findViewById(R.id.my_message);
        convertView.setTag(holder);
        return convertView;
    }


    private void senderView(Message message, MessageViewHolder holder, String messageBody) {
        TextView usernametv;
        holder.imageLeft.setVisibility(View.VISIBLE);
        holder.imageRight.setVisibility(View.GONE);
        holder.user.setVisibility(View.GONE);
        holder.sender.setVisibility(View.VISIBLE);
        holder.senderMessage.setVisibility(View.VISIBLE);
        holder.senderMessage.setText(messageBody);
        usernametv = (TextView) holder.sender.findViewById(R.id.sender_username);
        usernametv.setText(message.getUsername());
    }

    private void setUserView(MessageViewHolder holder, String messageBody) {
        AutoResizeTextView messageBodytv;
        holder.imageRight.setVisibility(View.VISIBLE);
        holder.imageLeft.setVisibility(View.GONE);
        holder.sender.setVisibility(View.GONE);
        holder.senderMessage.setVisibility(View.GONE);
        holder.user.setVisibility(View.VISIBLE);
        messageBodytv = (AutoResizeTextView) holder.user.findViewById(R.id.my_message_body);
        messageBodytv.setText(messageBody);
    }
}