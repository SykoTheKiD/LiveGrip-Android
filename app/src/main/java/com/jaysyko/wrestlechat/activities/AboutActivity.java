package com.jaysyko.wrestlechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jaysyko.wrestlechat.R;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final Button donate = (Button) findViewById(R.id.about_donate);
        final Button tweet = (Button) findViewById(R.id.about_tweet);

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donate = new Intent(getApplicationContext(), DonateActivity.class);
                startActivity(donate);
            }
        });

        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tweetIntent = new Intent(Intent.ACTION_SEND);
                tweetIntent.putExtra(Intent.EXTRA_TEXT, "Test; please ignore");
                tweetIntent.setType("application/twitter");
            }
        });
    }
}
