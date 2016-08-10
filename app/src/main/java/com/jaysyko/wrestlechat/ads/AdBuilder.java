package com.jaysyko.wrestlechat.ads;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.application.App;

/**
 * AdBuilder.java
 * Prepares and loads ads into the current activity
 *
 * @author Jay Syko
 */

public class AdBuilder {

    private Activity activity;
    private AdView mAdView;

    public AdBuilder(Activity activity) {
        this.activity = activity;
        MobileAds.initialize(activity, activity.getString(R.string.ads_app_id));
    }

    /**
     * Build and load ad into the current activity
     */
    public void buildAd() {
        if(!App.debug){
            mAdView = (AdView) activity.findViewById(R.id.ad_view);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);
        }
    }

    public void onResume(){
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    public void onPause(){
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

}
