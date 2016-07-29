package com.jaysyko.wrestlechat.sessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jaysyko.wrestlechat.analytics.AuthTracker;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceKeys;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;
import com.jaysyko.wrestlechat.utils.StringResources;

/**
 * Session.java
 * <p/>
 * A session object when the user logs in
 *
 * @author Jay Syko on 2016-06-26.
 */
public final class SessionManager {

    private static final String TAG = SessionManager.class.getSimpleName();

    /**
     * <p>Creates a new active session for a logged in user</p>
     *
     * @param context Context
     * @param user    User
     */
    public static void newSession(Context context, User user) {
        sync(context, user);
        Session.getInstance().setCurrentUser(user);
        AuthTracker.trackUser(user);

    }

    private synchronized static void sync(Context context, User user) {
        SharedPreferences.Editor editor = PreferenceProvider.getEditor(context, Preferences.SESSION);
        Gson userGson = new Gson();
        String userJson = userGson.toJson(user);
        editor.putString(PreferenceKeys.CURRENT_USER, userJson);
        editor.putBoolean(PreferenceKeys.IS_LOGGED_IN, true);
        editor.apply();
    }

    public synchronized static void sync(Context context) {
        sync(context, getCurrentUser());
    }


    /**
     * <p>Destroys the current user session (logged out)</p>
     *
     * @param context Context
     */
    public static void destroySession(Context context) {
        Session.getInstance().endSession();
        SharedPreferences.Editor editor = PreferenceProvider.getEditor(context, Preferences.SESSION);
        editor.clear();
        editor.apply();
        eLog.i(TAG, String.valueOf(isLoggedIn(context)));
    }

    /**
     * <p>Checks to see if a previously created session is available</p>
     *
     * @return boolean | Previously logged in our not
     */
    public static boolean isLoggedIn(Context context) {
        boolean loggedIn = PreferenceProvider.getSharedPreferences(context, Preferences.SESSION).getBoolean(PreferenceKeys.IS_LOGGED_IN, false);
        if (loggedIn) {
            loggedIn = restore(context);
        }
        return loggedIn;
    }

    /**
     * <p>Restore a user object from a previous session</p>
     *
     * @return user
     */
    private static boolean restore(Context context) {
        try {
            Gson gson = new Gson();
            final SharedPreferences sessionSharedPrefs = PreferenceProvider.getSharedPreferences(context, Preferences.SESSION);
            String json = sessionSharedPrefs.getString(PreferenceKeys.CURRENT_USER, StringResources.NULL_TEXT);
            if (!json.equals(StringResources.NULL_TEXT)) {
                User user = gson.fromJson(json, User.class);
                final Session session = Session.getInstance();
                session.setCurrentUser(user);
                return true;
            }
        } catch (Exception e) {
            eLog.e(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * <p>Gets the Current Session's user</p>
     *
     * @return User
     */
    public static User getCurrentUser() {
        return Session.getInstance().getCurrentUser();
    }
}
