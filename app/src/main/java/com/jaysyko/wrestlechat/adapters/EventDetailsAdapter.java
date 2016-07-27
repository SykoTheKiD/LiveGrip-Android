package com.jaysyko.wrestlechat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.viewholders.EventDetailsViewHolder;
import com.jaysyko.wrestlechat.models.Event;

import java.util.List;

/**
 * Created by jarushaan on 2016-06-13
 */
public class EventDetailsAdapter extends ArrayAdapter<Event.EventCard> {

    private static final int ZERO = 0;

    public EventDetailsAdapter(Context context, List<Event.EventCard> details) {
        super(context, ZERO, details);
    }

    /**
     * Gets the current view the adapter is working under
     *
     * @param position    int
     * @param convertView View
     * @param parent      ViewGroup
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(parent);
        }
        Event.EventCard detail = getItem(position);
        final EventDetailsViewHolder holder = (EventDetailsViewHolder) convertView.getTag();
        holder.detail.setText(detail.getSegment());
        final Integer rating = detail.getRating();
        final String additionalDetail = detail.getAdditionalDetail();
        if (additionalDetail != null) {
            holder.additionalDetail.setText(additionalDetail);
        }
        holder.ratingBar.setRating(rating);
        return convertView;
    }

    /**
     * Inflates the current view
     *
     * @param parent ViewGroup
     * @return View
     */
    @NonNull
    private View getView(ViewGroup parent) {
        View convertView = LayoutInflater.from(getContext()).
                inflate(R.layout.event_detail, parent, false);
        final EventDetailsViewHolder holder = new EventDetailsViewHolder();
        holder.detail = (TextView) convertView.findViewById(R.id.event_detail);
        holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        holder.additionalDetail = (TextView) convertView.findViewById(R.id.additional_detail);
        convertView.setTag(holder);
        return convertView;
    }
}
