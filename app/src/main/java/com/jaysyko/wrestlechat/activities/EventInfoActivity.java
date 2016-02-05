package com.jaysyko.wrestlechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.models.intentKeys.IntentKeys;
import com.jaysyko.wrestlechat.utils.DateChecker;
import com.jaysyko.wrestlechat.utils.Resources;
import com.squareup.picasso.Picasso;

public class EventInfoActivity extends AppCompatActivity {

    private static final String SHARE_DIALOG_TITLE = "Share Event";
    private String eventInfo, matchCardText, location, imageLink;
    private long startTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra(IntentKeys.EVENT_NAME));
        Intent intent = getIntent();
        eventInfo = intent.getStringExtra(IntentKeys.EVENT_INFO);
        imageLink = intent.getStringExtra(IntentKeys.EVENT_IMAGE);
        matchCardText = intent.getStringExtra(IntentKeys.EVENT_CARD);
        startTime = intent.getLongExtra(IntentKeys.EVENT_START_TIME, 0L);
        location = intent.getStringExtra(IntentKeys.EVENT_LOCATION);
        prepareEventInfoContent(getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType(Resources.PLAIN_CONTENT_TYPE);
                share.putExtra(Intent.EXTRA_TEXT, "Hey check out the {{ EVENT_NAME }} - sent from WrestleChat");
                startActivity(Intent.createChooser(share, SHARE_DIALOG_TITLE));
            }
        });
    }

    private void prepareEventInfoContent(Context context) {
        TextView eventDescription = (TextView) findViewById(R.id.event_info_description);
        eventDescription.setText(eventInfo);
        TextView matchCard = (TextView) findViewById(R.id.event_info_match_card);
        matchCardText = matchCardText.replace("\\n", System.getProperty("line.separator"));
        matchCard.setText(matchCardText);
        TextView startTimeTV = (TextView) findViewById(R.id.event_info_start_time);
        startTimeTV.setText(DateChecker.format(startTime));
        TextView locationTV = (TextView) findViewById(R.id.event_info_location);
        locationTV.setText(location);
        ImageView eventImage = (ImageView)findViewById(R.id.event_info_photo);
        Picasso.with(context).load(Resources.IMGUR_LINK.concat(imageLink)).into(eventImage);
    }
}
