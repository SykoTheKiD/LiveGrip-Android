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
import com.jaysyko.wrestlechat.dataObjects.Event;
import com.jaysyko.wrestlechat.utils.DateChecker;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private static final String IMGUR_LINK = "http://i.imgur.com/";
    private List<Event> itemsData;
    private Context context;


    public EventListAdapter(List<Event> itemsData, Context context) {
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
        Event currentCard = itemsData.get(position);
        viewHolder.txtViewTitle.setText(currentCard.getEventName());
        viewHolder.txtViewLocation.setText(currentCard.getLocation());
        Picasso.with(this.context).load(IMGUR_LINK.concat(currentCard.getImageLink())).into(viewHolder.imgViewIcon);

        if (DateChecker.goLive(currentCard.getStartTime())) {
            viewHolder.txtViewLiveStatus.setText(R.string.online_status_live);
        } else {
            String eventDate = DateChecker.format(currentCard.getStartTime());
            viewHolder.txtViewLiveStatus.setTextColor(Color.parseColor("#bdbdbd"));
            viewHolder.txtViewLiveStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
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