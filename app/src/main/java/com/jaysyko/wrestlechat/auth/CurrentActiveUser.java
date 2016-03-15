package com.jaysyko.wrestlechat.auth;

import android.util.Log;

import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * CurrentActiveUser.java
 * A single instance for the currently logged in user
 *
 * @author Jay Syko
 */
public class CurrentActiveUser {
    private static CurrentActiveUser activeCurrentActiveUser;
    private String username;
    private String password;
    private String profileImageURL;

    private CurrentActiveUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns a new instance of a current logged in user
     *
     * @param username String
     * @param password String
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser getInstance(String username, String password) {
        if (activeCurrentActiveUser == null) {
            activeCurrentActiveUser = new CurrentActiveUser(username, password);
        }
        return activeCurrentActiveUser;
    }

    /**
     * Returns the current instance of the logged in user
     *
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser getInstance() {
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            activeCurrentActiveUser = new CurrentActiveUser(currentUser.getUsername(), null);
        }
        return activeCurrentActiveUser;
    }

    public boolean setPassword(String password) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.setPassword(password);
        try {
            currentUser.save();
            activeCurrentActiveUser = null;
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Get the user defined imageUrl else return an generic image
     *
     * @return imageUrl: String
     */
    public String getCustomProfileImageURL() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String userImage = currentUser.getString(User.IMG_ID);
        if (userImage != null) {
            activeCurrentActiveUser.profileImageURL = userImage;
        } else {
            activeCurrentActiveUser.profileImageURL = ImageTools.defaultProfileImage(activeCurrentActiveUser.getUsername());
        }
        return activeCurrentActiveUser.profileImageURL;
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
        ParseUser currentUser = ParseUser.getCurrentUser();
        String oldUsername = activeCurrentActiveUser.username;
        currentUser.setUsername(username);
        try {
            currentUser.save();
            activeCurrentActiveUser.username = username;
            return true;
        } catch (ParseException e) {
            currentUser.setUsername(oldUsername);
            activeCurrentActiveUser.username = oldUsername;
            return false;
        }
    }

    /**
     * Login the user; returns true if successful else returns false
     *
     * @return boolean
     */
    public boolean loginUser() {
        try {
            ParseUser.logIn(activeCurrentActiveUser.username, activeCurrentActiveUser.password);
            return true;
        } catch (ParseException e) {
            activeCurrentActiveUser = null;
            return false;
        }

    }

    /**
     * Logs out the current user
     */
    public void logout() {
        activeCurrentActiveUser = null;
        ParseUser.logOut();
    }

    /**
     * Set Custom User Image
     *
     * @param url String
     * @return String
     */
    public boolean setProfileImageURL(final String url) {
        Boolean isLinkToImage = ImageTools.isLinkToImage(url);
        if (isLinkToImage) {
            activeCurrentActiveUser.profileImageURL = url;
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.put(User.IMG_ID, url);
            try {
                currentUser.save();
                return true;
            } catch (ParseException e) {
                Log.e("FAIL IMAGE", e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    public String getUserID() {
        return null;
    }
}
