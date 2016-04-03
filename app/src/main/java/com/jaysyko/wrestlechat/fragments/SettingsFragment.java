package com.jaysyko.wrestlechat.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jaysyko.wrestlechat.R;

/**
 * @author Jay Syko
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

