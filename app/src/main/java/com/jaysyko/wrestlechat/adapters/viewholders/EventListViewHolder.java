package com.jaysyko.wrestlechat.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.application.eLog;

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
    public ImageButton addToCalendar;

    public EventListViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.event_title);
        imgViewIcon = (KenBurnsView) itemLayoutView.findViewById(R.id.event_image);
        txtViewLocation = (TextView) itemLayoutView.findViewById(R.id.event_location);
        txtViewLiveStatus = (TextView) itemLayoutView.findViewById(R.id.event_time);
        addToCalendar = (ImageButton) itemLayoutView.findViewById(R.id.add_event_notification);

        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eLog.e("TEST", "PRESSED");
            }
        });
    }
}
