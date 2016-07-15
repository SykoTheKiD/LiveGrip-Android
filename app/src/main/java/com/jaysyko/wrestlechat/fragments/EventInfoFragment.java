package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.adapters.EventDetailsAdapter;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.eventManager.CurrentActiveEvent;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.utils.ImageTools;

public class EventInfoFragment extends Fragment {

    private final Event currentActiveEvent = CurrentActiveEvent.getInstance().getCurrentEvent();
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_info, container, false);
        /**
         * Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
         image.startAnimation(animation1);
         */
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        final Activity activity = getActivity();
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        prepareEventInfoContent(activity);
        ListView eventDetails = (ListView) view.findViewById(R.id.events_list_view);
        eventDetails.setEmptyView(view.findViewById(R.id.empty_layout));
        EventDetailsAdapter adapter = new EventDetailsAdapter(getActivity(), currentActiveEvent.getMatchList());
        eventDetails.setAdapter(adapter);
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                new AdBuilder(activity).buildAd();
//            }
//        });
        return view;
    }

    private void prepareEventInfoContent(Context context) {
        TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        eventTitle.setText(currentActiveEvent.getEventName());
        TextView startTimeTV = (TextView) view.findViewById(R.id.event_time);
        startTimeTV.setText(DateVerifier.format(currentActiveEvent.getEventStartTime()));
        TextView locationTV = (TextView) view.findViewById(R.id.event_location);
        locationTV.setText(currentActiveEvent.getEventLocation());
        ImageView eventImage = (ImageView) view.findViewById(R.id.event_image);
        ImageTools.loadImage(context, currentActiveEvent.getEventImage(), eventImage);
    }

}
