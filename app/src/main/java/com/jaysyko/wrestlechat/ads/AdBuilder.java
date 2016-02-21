package com.jaysyko.wrestlechat.ads;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jaysyko.wrestlechat.R;

/**
 * AdBuilder.java
 * Prepares and loads ads into the current activity
 *
 * @author Jay Syko
 */

public class AdBuilder {

    private Activity activity;

    public AdBuilder(Activity activity) {
        this.activity = activity;
    }

    /**
     * Build and load ad into the current activity
     */
    public void buildAd() {
        AdView adView = (AdView) this.activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
