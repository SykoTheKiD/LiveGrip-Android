package com.jaysyko.wrestlechat.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;

public class EventListViewHolder extends RecyclerView.ViewHolder {
    public TextView txtViewTitle;
    public ImageView imgViewIcon;
    public TextView txtViewLocation, txtViewLiveStatus;

    public EventListViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.event_name);
        imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.event_image);
        txtViewLocation = (TextView) itemLayoutView.findViewById(R.id.event_location);
        txtViewLiveStatus = (TextView) itemLayoutView.findViewById(R.id.event_live);
    }
}
