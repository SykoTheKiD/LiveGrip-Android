package com.jaysyko.wrestlechat.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.fragments.ColourPickerFragment;

public class ColourPickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_picker);
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container_view);

        if (fragment == null) {
            fragment = new ColourPickerFragment();
            fm.beginTransaction()
                    .add(R.id.container_view, fragment)
                    .commit();
        }
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

}
