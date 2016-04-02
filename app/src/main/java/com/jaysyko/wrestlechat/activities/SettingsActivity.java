package com.jaysyko.wrestlechat.activities;


import android.os.Bundle;

import com.jaysyko.wrestlechat.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

}
