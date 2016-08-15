package com.jaysyko.wrestlechat.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceKeys;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;

/**
 * @author Jay Syko
 */

public class SettingsFragment extends PreferenceFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    public static final String COLOUR_PICKER_TITLE = "Choose Colour";
    public static final String DEFAULT_COLOUR = "#ff0000";
    public static final String POSITIVE_TITLE = "ok";
    public static final String NEGATIVE_TITLE = "cancel";
    private final Handler handler = new Handler();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference dialogPreference = getPreferenceScreen().findPreference(PreferenceKeys.COLOUR_PREFERENCE);
        dialogPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                ColorPickerDialogBuilder
                        .with(getActivity())
                        .setTitle(COLOUR_PICKER_TITLE)
                        .initialColor(Color.parseColor(DEFAULT_COLOUR))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setPositiveButton(POSITIVE_TITLE, new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int selectedColor, Integer[] allColors) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences.Editor editor = PreferenceProvider.getEditor(getActivity(), Preferences.SETTINGS);
                                        editor.putInt(PreferenceKeys.MESSAGING_BUBBLE_COLOUR, selectedColor);
                                        editor.apply();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(NEGATIVE_TITLE, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eLog.i(TAG, "Cancelled Colour Selection");
                            }
                        })
                        .build()
                        .show();
                return true;
            }
        });
    }
}

