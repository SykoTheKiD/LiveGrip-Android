package com.jaysyko.wrestlechat.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jaysyko.wrestlechat.utils.ImageTools;

import org.json.JSONArray;
import org.json.JSONException;
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
    private static final String TAG = CurrentActiveUser.class.getSimpleName();
    private static CurrentActiveUser activeUser;
    private Context context;
    private String userID;
    private String username;
    private String password;
    private String profileImageURL;

    private CurrentActiveUser(String userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns a new instance of a current logged in user
     *
     * @param context String
     * @param payload String
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser newUser(Context context, String password, JSONArray payload) {
        try {
            JSONObject user = (JSONObject) payload.get(0);
            String userID = user.getString(UserKeys.ID.toString());
            String username = user.getString(UserKeys.USERNAME.toString());
            String profileImageURL = user.getString(UserKeys.PROFILE_IMAGE.toString());
            SharedPreferences sharedPreferences = SessionManager.getSessionManager(context).getSharedPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LOGGED_IN, true);
            editor.putString(USERNAME, username);
            editor.putString(USER_ID, userID);
            editor.putString(PASSWORD, password);
            editor.putString(IMAGE_URL, profileImageURL);
            editor.apply();
            activeUser = new CurrentActiveUser(userID, username, password);
            activeUser.context = context;
            activeUser.setUsername(username);
            activeUser.setUserId(userID);
            activeUser.setProfileImageURL(profileImageURL);
            activeUser.setPassword(password);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return activeUser;
    }

    /**
     * Returns the current instance of the logged in user
     *
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser getCurrentUser() {
        return activeUser;
    }


    public static CurrentActiveUser getCurrentUser(Context context) {
        SharedPreferences sharedPreferences = SessionManager.getSessionManager(context).getSharedPreferences();
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            String storedUserID = sharedPreferences.getString(USER_ID, null);
            String storedUsername = sharedPreferences.getString(USERNAME, null);
            String storedPassword = sharedPreferences.getString(PASSWORD, null);
            String storedImageURL = sharedPreferences.getString(IMAGE_URL, null);
            activeUser = new CurrentActiveUser(storedUserID, storedUsername, storedPassword);
            activeUser.setProfileImageURL(storedImageURL);
            activeUser.context = context;
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
        return activeUser.username;
    }

    public boolean setUsername(String username) {
        activeUser.username = username;
        return true;
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        SharedPreferences sharedPreferences = SessionManager.getSessionManager(activeUser.context).getSharedPreferences();
        sharedPreferences.edit().clear();
        sharedPreferences.edit().apply();
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

    private void setUserId(String userId) {
        activeUser.userID = userId;
    }

    private String getPassword() {
        return activeUser.password;
    }
}
