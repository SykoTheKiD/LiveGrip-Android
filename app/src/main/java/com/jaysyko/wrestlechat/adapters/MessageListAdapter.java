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
import com.jaysyko.wrestlechat.models.db.ParseMessageModel;
import com.jaysyko.wrestlechat.models.db.ParseUserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageListAdapter extends ArrayAdapter<ParseMessageModel> {
    private String mUserId;

    public MessageListAdapter(Context context, String userId, List<ParseMessageModel> parseMessageModels) {
        super(context, 0, parseMessageModels);
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
        final ParseMessageModel parseMessageModel = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
//        final String userName = parseMessageModel.get
        final boolean isMe = parseMessageModel.getUserId().equals(mUserId);
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            holder.body.setBackgroundResource(R.drawable.speech_bubble_green);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.body.setBackgroundResource(R.drawable.speech_bubble_orange);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
        Picasso.with(getContext()).load(ParseUserModel.getProfileUrl(parseMessageModel.getUserId())).into(profileView);
        holder.body.setText(parseMessageModel.getBody());
        return convertView;
    }    // Create a gravatar image based on the hash value obtained from userId

    final class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
    }

}