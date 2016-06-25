package com.jaysyko.wrestlechat.sessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.utils.StringResources;

/**
 * Session.java
 * A session object that holds an active session for a logged in user
 *
 * @author Jay Syko
 */
public class Session {

    public static final String CURRENT_USER = "currentUser";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String SESSION = "session";
    private static Session instance;
    private User currentUser;
    private SharedPreferences sharedPreferences;

    private Session(Context context) {
        sharedPreferences = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
    }

    /**
     * Get an instance of the current session
     *
     * @return Session
     */
    public static Session getInstance() {
        return instance;
    }

    /**
     * Creates a new active session for a logged in user
     *
     * @param context Context
     * @param user    User
     */
    public void newSession(Context context, User user) {
        if (currentUser == null) {
            setCurrentUser(user);
            instance = new Session(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson userGson = new Gson();
            String userJson = userGson.toJson(user);
            editor.putString(CURRENT_USER, userJson);
            editor.putBoolean(IS_LOGGED_IN, true);
            editor.apply();
        }
    }

    /**
     * Destroys the current user session (logged out)
     */
    public void destroySession() {
        currentUser = null;
        instance = null;
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    /**
     * Checks to see if a previously created session is available
     *
     * @param context Context
     * @return boolean | Previously logged in our not
     */
    public boolean isLoggedIn(Context context) {
        instance = new Session(context);
        boolean loggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
        if (loggedIn) {
            loggedIn = restore(context);
        } else {
            instance = null;
        }
        return loggedIn;
    }

    /**
     * Returns the current user associated with the current session
     *
     * @return User currentUser
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current active user associated with this session
     *
     * @param user User
     */
    private void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Restore a user object from a previous session
     *
     * @param context Context
     * @return user
     */
    private boolean restore(Context context) {
        instance = new Session(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CURRENT_USER, StringResources.NULL_TEXT);
        if (!json.equals(StringResources.NULL_TEXT)) {
            User user = gson.fromJson(json, User.class);
            setCurrentUser(user);
            return true;
        }
        return false;
    }

}
