package com.jaysyko.wrestlechat.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.jaysyko.wrestlechat.R;

/**
 * EventListViewHolder.java
 * ViewHolder for the event list object
 *
 * @author Jay Syko
 */
public final class EventListViewHolder extends RecyclerView.ViewHolder {
    public TextView txtViewTitle;
    public KenBurnsView imgViewIcon;
    public TextView txtViewLocation, txtViewLiveStatus;

    public EventListViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.event_title);
        imgViewIcon = (KenBurnsView) itemLayoutView.findViewById(R.id.event_image);
        txtViewLocation = (TextView) itemLayoutView.findViewById(R.id.event_location);
        txtViewLiveStatus = (TextView) itemLayoutView.findViewById(R.id.event_time);
    }
}
