package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.models.Message;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageListAdapter extends ArrayAdapter<Message> {
    private String mUserId;

    public MessageListAdapter(Context context, String userId, List<Message> messages) {
        super(context, 0, messages);
        this.mUserId = userId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.message_bubble, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.imageLeft = (ImageView) convertView.findViewById(R.id.ivProfileLeft);
            holder.imageRight = (ImageView) convertView.findViewById(R.id.ivProfileRight);
            holder.sender = (RelativeLayout) convertView.findViewById(R.id.sender_message);
            holder.user = (RelativeLayout) convertView.findViewById(R.id.my_message);
            convertView.setTag(holder);
        }
        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final boolean isMe = message.getUsername().equals(mUserId);
        final String messageBody = message.getBody();
        TextView messageBodytv;
        TextView usernametv;
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            holder.sender.setVisibility(View.GONE);
            holder.user.setVisibility(View.VISIBLE);
            messageBodytv = (TextView) holder.user.findViewById(R.id.my_message_body);
            messageBodytv.setText(messageBody);

        } else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.user.setVisibility(View.GONE);
            holder.sender.setVisibility(View.VISIBLE);
            messageBodytv = (TextView) holder.sender.findViewById(R.id.sender_message_body);
            messageBodytv.setText(messageBody);
            usernametv = (TextView) holder.sender.findViewById(R.id.sender_username);
            usernametv.setText(message.getUsername());

        }
        final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
        Picasso.with(getContext()).load(ImageTools.getProfileImage(message.getUsername())).into(profileView);
        return convertView;
    }

    final class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public RelativeLayout sender;
        public RelativeLayout user;

    }

}