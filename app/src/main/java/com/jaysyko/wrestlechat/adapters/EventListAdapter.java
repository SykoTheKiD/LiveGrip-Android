package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.viewholders.EventListViewHolder;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.List;

/**
 * EventListAdapter.java
 * Adapter that holds the event list items
 *
 * @author Jay Syko
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListViewHolder> {
    private static final String NON_LIVE_TEXT_COLOUR = "#bdbdbd";
    private static final int NON_LIVE_TEXT_SIZE = 15;
    private static final String LIVE_TEXT_COLOUR = "#00C853";
    private static final int LIVE_TEXT_SIZE = 20;
    public List<Event> itemsData;
    private Context context;


    public EventListAdapter(List<Event> itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    /**
     * Create new views (invoked by the layout manager)
     * @param parent ViewGroup
     * @param viewType int
     * @return EventListViewHolder
     */
    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, null);

        // create ViewHolder

        return new EventListViewHolder(itemLayoutView);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * @param viewHolder EventListViewHolder
     * @param position int
     */
    @Override
    public void onBindViewHolder(final EventListViewHolder viewHolder, int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Event currentCard = itemsData.get(position);
        Log.i("TERM" , currentCard.getEventName());
        Log.i("TERM" , currentCard.getEventImage());
        viewHolder.txtViewTitle.setText(currentCard.getEventName());
        viewHolder.txtViewLocation.setText(currentCard.getEventLocation());
        ImageTools.loadImage(this.context, StringResources.IMGUR_LINK.concat(currentCard.getEventImage()), viewHolder.imgViewIcon);

        if (DateVerifier.goLive(currentCard.getEventStartTime(), currentCard.getEventEndTime()).goLive()) {
            viewHolder.txtViewLiveStatus.setText(R.string.online_status_live);
            viewHolder.txtViewLiveStatus.setTextColor(Color.parseColor(LIVE_TEXT_COLOUR));
            viewHolder.txtViewLiveStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, LIVE_TEXT_SIZE);
        } else {
            String eventDate = DateVerifier.format(currentCard.getEventStartTime());
            viewHolder.txtViewLiveStatus.setTextColor(Color.parseColor(NON_LIVE_TEXT_COLOUR));
            viewHolder.txtViewLiveStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, NON_LIVE_TEXT_SIZE);
            viewHolder.txtViewLiveStatus.setText(eventDate);
        }
    }

    /**
     * Return the size of your itemsData (invoked by the layout manager)
     * @return int
     */
    @Override
    public int getItemCount() {
        return this.itemsData.size();
    }
}