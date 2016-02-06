package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.auth.CurrentActiveUser;
import com.jaysyko.wrestlechat.models.Message;
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
            holder.body = (TextView) convertView.findViewById(R.id.tvMessage);
            convertView.setTag(holder);
        }
        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        final boolean isMe = message.getUsername().equals(mUserId);
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            holder.body.setBackgroundResource(R.drawable.in_message_bg);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        } else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.body.setBackgroundResource(R.drawable.out_message_bg);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        }
        final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
        Picasso.with(getContext()).load(CurrentActiveUser.getInstance().getProfileImage()).into(profileView);
        holder.body.setText(message.getBody());
        return convertView;
    }

    final class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
    }

}