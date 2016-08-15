package com.jaysyko.wrestlechat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.application.App;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView buildVersion = (TextView) findViewById(R.id.build_version);
        if (buildVersion != null) {
            String buildVersionString = "Build Version: " + App.APP_VERSION;
            buildVersion.setText(buildVersionString);
        }
    }
}
