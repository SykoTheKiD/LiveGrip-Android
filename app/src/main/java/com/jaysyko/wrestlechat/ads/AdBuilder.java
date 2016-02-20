package com.jaysyko.wrestlechat.ads;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jaysyko.wrestlechat.R;


public class AdBuilder {

    private Activity activity;

    public AdBuilder(Activity activity) {
        this.activity = activity;
    }

    public void buildAd() {
        AdView adView = (AdView) this.activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
    }
}
