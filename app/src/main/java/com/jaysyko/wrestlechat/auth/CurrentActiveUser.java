package com.jaysyko.wrestlechat.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jaysyko.wrestlechat.localStorage.LocalStorage;
import com.jaysyko.wrestlechat.localStorage.StorageFile;
import com.jaysyko.wrestlechat.utils.ImageTools;

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
    private static final String IMAGE_URL = "imageURL";
    private static final String TAG = CurrentActiveUser.class.getSimpleName();
    private static CurrentActiveUser activeUser;
    private Context context;
    private String userID;
    private String username;
    private String profileImageURL;

    private CurrentActiveUser(String userID, String username) {
        this.userID = userID;
        this.username = username;
    }

    /**
     * Returns a new instance of a current logged in user
     *
     * @param context String
     * @param payload String
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser newUser(Context context, JSONObject payload) {
        try {
            String userID = payload.getString(UserKeys.ID.toString());
            String username = payload.getString(UserKeys.USERNAME.toString());
            String profileImageURL = payload.getString(UserKeys.PROFILE_IMAGE.toString());
            SharedPreferences sharedPreferences = new LocalStorage(context, StorageFile.AUTH).getSharedPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LOGGED_IN, true);
            editor.putString(USERNAME, username);
            editor.putString(USER_ID, userID);
            editor.putString(IMAGE_URL, profileImageURL);
            editor.apply();
            activeUser = new CurrentActiveUser(userID, username);
            activeUser.context = context;
            activeUser.setUsername(username);
            activeUser.setUserId(userID);
            activeUser.setProfileImageURL(profileImageURL);
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
        SharedPreferences sharedPreferences = new LocalStorage(context, StorageFile.AUTH).getSharedPreferences();
        if (sharedPreferences.getBoolean(IS_LOGGED_IN, false)) {
            String storedUserID = sharedPreferences.getString(USER_ID, null);
            String storedUsername = sharedPreferences.getString(USERNAME, null);
            String storedImageURL = sharedPreferences.getString(IMAGE_URL, null);
            activeUser = new CurrentActiveUser(storedUserID, storedUsername);
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

    /**
     * Get the user defined imageUrl else return an generic image
     *
     * @return imageUrl: String
     */
    public String getProfileImage() {
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
        SharedPreferences sharedPreferences = new LocalStorage(context, StorageFile.AUTH).getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
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

}
