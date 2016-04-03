package com.jaysyko.wrestlechat.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.utils.StringResources;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        final Button tweet = (Button) findViewById(R.id.about_tweet);
        if (tweet != null) {
            tweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = StringResources.TWITTER_INTENT.concat(getString(R.string.app_share));
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }
    }
}
