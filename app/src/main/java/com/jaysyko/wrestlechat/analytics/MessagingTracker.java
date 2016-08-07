package com.jaysyko.wrestlechat.analytics;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.jaysyko.wrestlechat.application.App;
import com.jaysyko.wrestlechat.models.Event;

/**
 * @author Jay Syko on 2016-07-18.
 */
public class MessagingTracker {

    private static final String APP_VERSION = "app_version";

    public static void trackEvent(Event event){
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(event.getEventName())
                .putCustomAttribute(APP_VERSION, App.APP_VERSION));
    }
}
