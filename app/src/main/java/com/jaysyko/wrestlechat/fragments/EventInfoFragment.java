package com.jaysyko.wrestlechat.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.activeEvent.CurrentActiveEvent;
import com.jaysyko.wrestlechat.ads.AdBuilder;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.StringResources;

public class EventInfoFragment extends Fragment {

    private static final String LINE_SEPARATOR = "line.separator";
    private static final String NEW_LINE = "\\n";
    private final CurrentActiveEvent currentActiveEvent = CurrentActiveEvent.getInstance();
    private String eventName, eventInfo, matchCardText, location, imageLink;
    private long startTime;
    private Handler handler = new Handler();
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
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        final Activity activity = getActivity();
        ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        eventName = CurrentActiveEvent.getInstance().getEventName();
        activity.setTitle(StringResources.NULL_TEXT);
        eventInfo = currentActiveEvent.getEventInfo();
        imageLink = currentActiveEvent.getEventImage();
        matchCardText = currentActiveEvent.getMatchCard();
        startTime = currentActiveEvent.getEventStartTime();
        location = currentActiveEvent.getEventLocation();
        prepareEventInfoContent(activity);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(StringResources.PLAIN_CONTENT_TYPE);
                share.putExtra(Intent.EXTRA_TEXT, getShareMessage(eventName));
                startActivity(Intent.createChooser(share, getString(R.string.app_share_title)));
            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                new AdBuilder(activity).buildAd();
            }
        });
        return view;
    }

    @NonNull
    private String getShareMessage(String eventName) {
        return getString(R.string.hey_check_out).concat(StringResources.BLANK_SPACE).concat(eventName).concat(getString(R.string.sent_from_wrestlechat));
    }

    private void prepareEventInfoContent(Context context) {
        TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        eventTitle.setText(eventName);
        TextView eventDescription = (TextView) view.findViewById(R.id.event_info_description);
        eventDescription.setText(eventInfo);
        TextView matchCard = (TextView) view.findViewById(R.id.event_info_match_card);
        matchCardText = matchCardText.replace(NEW_LINE, System.getProperty(LINE_SEPARATOR));
        matchCard.setText(matchCardText);
        TextView startTimeTV = (TextView) view.findViewById(R.id.event_info_start_time);
        startTimeTV.setText(DateVerifier.format(startTime));
        TextView locationTV = (TextView) view.findViewById(R.id.event_info_location);
        locationTV.setText(location);
        ImageView eventImage = (ImageView) view.findViewById(R.id.event_info_photo);
        ImageTools.loadImage(context, StringResources.IMGUR_LINK.concat(imageLink), eventImage);
    }

}
