package com.jaysyko.wrestlechat.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.jaysyko.wrestlechat.utils.ImageTools;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * CurrentActiveUser.java
 * A single instance for the currently logged in user
 *
 * @author Jay Syko
 */
public class CurrentActiveUser {
    private static final String USERNAME = "username";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String USER_ID = "userID";
    private static final String PASSWORD = "password";
    private static final String IMAGE_URL = "imageURL";
    private static CurrentActiveUser activeUser;
    private String userID;
    private String username;
    private String password;
    private String profileImageURL;

    private CurrentActiveUser(String userID, String username, String password, String profileImageURL) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.profileImageURL = profileImageURL;
    }

    /**
     * Returns a new instance of a current logged in user
     *
     * @param context String
     * @param payload String
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser newUser(Context context, JSONArray payload) {
        SharedPreferences sharedPreferences = SessionManager.getSessionManager(context).getSharedPreferences();
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            String storedUserID = sharedPreferences.getString(USER_ID, null);
            String storedUsername = sharedPreferences.getString(USERNAME, null);
            String storedPassword = sharedPreferences.getString(PASSWORD, null);
            String storedImageURL = sharedPreferences.getString(IMAGE_URL, null);
            activeUser = new CurrentActiveUser(storedUserID, storedUsername, storedPassword, storedImageURL);
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USERNAME, activeUser.getUsername());
            editor.putString(USER_ID, activeUser.getUserID());
            editor.putString(PASSWORD, activeUser.password);
            editor.apply();
            activeUser = new CurrentActiveUser(userID, username, password);
        }
        return activeUser;
    }

    /**
     * Returns the current instance of the logged in user
     *
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser getCurrentUser() {
        SharedPreferences sharedPreferences = SessionManager.getSessionManager(context).getSharedPreferences();
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            String storedUserID = sharedPreferences.getString(USER_ID, null);
            String storedUsername = sharedPreferences.getString(USERNAME, null);
            String storedPassword = sharedPreferences.getString(PASSWORD, null);
            String storedImageURL = sharedPreferences.getString(IMAGE_URL, null);
            activeUser = new CurrentActiveUser(storedUserID, storedUsername, storedPassword, storedImageURL);
        }
        return activeUser;
    }

    /**
     * @return userID
     */
    public String getUserID() {
        return activeUser.userID;
    }

    public boolean setPassword(String password) {
        activeUser.password = password;
        return true;
    }

    /**
     * Get the user defined imageUrl else return an generic image
     *
     * @return imageUrl: String
     */
    public String getCustomProfileImageURL() {
        return activeUser.profileImageURL;
    }

    /**
     * Return the current user's username
     *
     * @return username: String
     */
    public String getUsername() {
        return username;
    }

    public boolean setUsername(String username) {
        // hit /users/edit
//        ac.setUsername(username);
        return true;
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        // delete local session
        activeUser = null;
    }

    /**
     * Set Custom User Image
     *
     * @param url String
     * @return String
     */
    public boolean setProfileImageURL(final String url) {
        // store url in local cache
        Boolean isLinkToImage = ImageTools.isLinkToImage(url);
        if (isLinkToImage) {
            activeUser.profileImageURL = url;
            return true;
        } else {
            return false;
        }
    }
}
