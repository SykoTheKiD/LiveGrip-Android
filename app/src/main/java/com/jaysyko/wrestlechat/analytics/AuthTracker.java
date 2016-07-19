package com.jaysyko.wrestlechat.analytics;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SignUpEvent;
import com.jaysyko.wrestlechat.application.App;

/**
 * @author Jay Syko on 2016-07-18.
 */
public class AuthTracker {
    private static final String NATIVE_SIGN_UP = "native_signUp";
    private static final String NATIVE_LOGIN = "native_login";
    private static final String APP_VERSION = "app_version";

    public static void trackLogin(boolean success){
        Answers.getInstance().logLogin(new LoginEvent()
                .putMethod(NATIVE_LOGIN)
                .putCustomAttribute(APP_VERSION, App.APP_VERSION)
                .putSuccess(success));
    }

    public static void trackSignUp(boolean success){
        Answers.getInstance().logSignUp(new SignUpEvent()
                .putMethod(NATIVE_SIGN_UP)
                .putCustomAttribute(APP_VERSION, App.APP_VERSION)
                .putSuccess(success));
    }
}
