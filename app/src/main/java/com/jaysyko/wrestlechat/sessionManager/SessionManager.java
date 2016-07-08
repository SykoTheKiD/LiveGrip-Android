package com.jaysyko.wrestlechat.sessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.utils.StringResources;

/**
 * @author Jay Syko on 2016-06-26.
 */
public class SessionManager {

    private static final String CURRENT_USER = "currentUser";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String SESSION = "session";

    /**
     * Creates a new active session for a logged in user
     *
     * @param context Context
     * @param user    User
     */
    public static void newSession(Context context, User user) {
//        SharedPreferences.Editor editor = getSessionSharedPrefs(context).edit();
//        Gson userGson = new Gson();
//        String userJson = userGson.toJson(user);
//        editor.putString(CURRENT_USER, userJson);
//        editor.putBoolean(IS_LOGGED_IN, true);
//        editor.apply();
        Session.getInstance().setCurrentUser(user);
    }

    /**
     * Destroys the current user session (logged out)
     *
     * @param context Context
     */
    public static void destroySession(Context context) {
        Session.getInstance().endSession();
        SharedPreferences.Editor editor = getSessionSharedPrefs(context).edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Checks to see if a previously created session is available
     *
     * @return boolean | Previously logged in our not
     */
    public static boolean isLoggedIn(Context context) {
        boolean loggedIn = getSessionSharedPrefs(context).getBoolean(IS_LOGGED_IN, false);
        if (loggedIn) {
            loggedIn = restore(context);
        }
        return loggedIn;
    }

    /**
     * Restore a user object from a previous session
     *
     * @return user
     */
    private static boolean restore(Context context) {
        Gson gson = new Gson();
        String json = getSessionSharedPrefs(context).getString(CURRENT_USER, StringResources.NULL_TEXT);
        if (!json.equals(StringResources.NULL_TEXT)) {
            User user = gson.fromJson(json, User.class);
            final Session session = Session.getInstance();
            if (session != null) {
                session.setCurrentUser(user);
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the SharedPreferences file for Session Management
     *
     * @param context Context
     * @return SharedPreferences
     */
    private static SharedPreferences getSessionSharedPrefs(Context context) {
        return context.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
    }

    /**
     * Gets the Current Session's user
     *
     * @return User
     */
    public static User getCurrentUser() {
        return Session.getInstance().getCurrentUser();
    }
}
