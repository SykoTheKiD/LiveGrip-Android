package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.dataObjects.EventObject;
import com.jaysyko.wrestlechat.utils.DateVerifier;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.Resources;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private static final String NON_LIVE_TEXT_COLOUR = "#bdbdbd";
    private static final int NON_LIVE_TEXT_SIZE = 15;
    private List<EventObject> itemsData;
    private Context context;


    public EventListAdapter(List<EventObject> itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, null);

        // create ViewHolder

        return new ViewHolder(itemLayoutView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        EventObject currentCard = itemsData.get(position);
        viewHolder.txtViewTitle.setText(currentCard.getEventName());
        viewHolder.txtViewLocation.setText(currentCard.getLocation());
        ImageTools.loadImage(this.context, Resources.IMGUR_LINK.concat(currentCard.getImageLink()), viewHolder.imgViewIcon);

        if (DateVerifier.goLive(currentCard.getStartTime())) {
            viewHolder.txtViewLiveStatus.setText(R.string.online_status_live);
        } else {
            String eventDate = DateVerifier.format(currentCard.getStartTime());
            viewHolder.txtViewLiveStatus.setTextColor(Color.parseColor(NON_LIVE_TEXT_COLOUR));
            viewHolder.txtViewLiveStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, NON_LIVE_TEXT_SIZE);
            viewHolder.txtViewLiveStatus.setText(eventDate);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.itemsData.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;
        public TextView txtViewLocation;
        public TextView txtViewLiveStatus;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.event_name);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.event_image);
            txtViewLocation = (TextView) itemLayoutView.findViewById(R.id.event_location);
            txtViewLiveStatus = (TextView) itemLayoutView.findViewById(R.id.event_live);
        }
    }
}