package com.jaysyko.wrestlechat.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.fragments.EventInfoFragment;

public class EventInfoActivity extends BaseActivity {

    @Override
    protected Fragment createFragment() {
        return new EventInfoFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
    }
}
