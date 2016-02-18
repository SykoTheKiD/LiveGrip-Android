package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.conversation.CurrentActiveEvent;
import com.jaysyko.wrestlechat.date.DateVerifier;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.StringResources;

public class EventInfoActivity extends AppCompatActivity {

    private static final String LINE_SEPARATOR = "line.separator";
    private final CurrentActiveEvent currentActiveEvent = CurrentActiveEvent.getInstance();
    private String eventName, eventInfo, matchCardText, location, imageLink;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        eventName = CurrentActiveEvent.getInstance().getEventName();
        setTitle(StringResources.NULL_TEXT);
        eventInfo = currentActiveEvent.getEventInfo();
        imageLink = currentActiveEvent.getEventImage();
        matchCardText = currentActiveEvent.getMatchCard();
        startTime = currentActiveEvent.getEventStartTime();
        location = currentActiveEvent.getEventLocation();
        prepareEventInfoContent(getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(StringResources.PLAIN_CONTENT_TYPE);
                share.putExtra(Intent.EXTRA_TEXT, getShareMessage(eventName));
                startActivity(Intent.createChooser(share, getString(R.string.app_share_title)));
            }
        });
    }

    @NonNull
    private String getShareMessage(String eventName) {
        return getString(R.string.hey_check_out).concat(StringResources.BLANK_SPACE).concat(eventName).concat(getString(R.string.sent_from_wrestlechat));
    }

    private void prepareEventInfoContent(Context context) {
        TextView eventTitle = (TextView) findViewById(R.id.event_title);
        eventTitle.setText(eventName);
        TextView eventDescription = (TextView) findViewById(R.id.event_info_description);
        eventDescription.setText(eventInfo);
        TextView matchCard = (TextView) findViewById(R.id.event_info_match_card);
        matchCardText = matchCardText.replace("\\n", System.getProperty(LINE_SEPARATOR));
        matchCard.setText(matchCardText);
        TextView startTimeTV = (TextView) findViewById(R.id.event_info_start_time);
        startTimeTV.setText(DateVerifier.format(startTime));
        TextView locationTV = (TextView) findViewById(R.id.event_info_location);
        locationTV.setText(location);
        ImageView eventImage = (ImageView)findViewById(R.id.event_info_photo);
        ImageTools.loadImage(context, StringResources.IMGUR_LINK.concat(imageLink), eventImage);
    }
}
