package com.jaysyko.wrestlechat.analytics;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;

/**
 * @author Jay Syko on 2016-07-18.
 */
public class ShareTracker {

    public static void trackShare(){
        Answers.getInstance().logShare(new ShareEvent()
                .putMethod("nav_drawer"));
    }
}
