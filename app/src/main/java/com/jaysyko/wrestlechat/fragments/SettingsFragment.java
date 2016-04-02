package com.jaysyko.wrestlechat.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jaysyko.wrestlechat.R;

/**
 * Created by jarushaan on 2016-04-02
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

