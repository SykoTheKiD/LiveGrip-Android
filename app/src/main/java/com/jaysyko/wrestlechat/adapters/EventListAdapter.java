package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.models.EventObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private static final String IMGUR_LINK = "http://i.imgur.com/";
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

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        EventObject currentCard = itemsData.get(position);
        viewHolder.txtViewTitle.setText(currentCard.getEventName());
        viewHolder.txtViewLocation.setText(currentCard.getLocation());
        Picasso.with(this.context).load(IMGUR_LINK.concat(currentCard.getImageLink())).into(viewHolder.imgViewIcon);
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

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.title);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.thumbnail);
            txtViewLocation = (TextView) itemLayoutView.findViewById(R.id.description);
        }
    }
}