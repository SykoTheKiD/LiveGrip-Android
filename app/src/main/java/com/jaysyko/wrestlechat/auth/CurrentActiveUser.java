package com.jaysyko.wrestlechat.auth;

import com.jaysyko.wrestlechat.utils.ImageTools;

/**
 * CurrentActiveUser.java
 * A single instance for the currently logged in user
 *
 * @author Jay Syko
 */
public class CurrentActiveUser {
    private static CurrentActiveUser activeCurrentActiveUser;
    private String userID;
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
    public static CurrentActiveUser getInstance(String userID, String username, String password) {
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
//        if (currentUser != null) {
//            activeCurrentActiveUser = new CurrentActiveUser(currentUser.getUsername(), null);
//        }
        return activeCurrentActiveUser;
    }

    /**
     * @return userID
     */
    public String getUserID() {
        return activeCurrentActiveUser.userID;
    }

    public boolean setPassword(String password) {
        activeCurrentActiveUser.password = password;
        return true;
    }

    /**
     * Get the user defined imageUrl else return an generic image
     *
     * @return imageUrl: String
     */
    public String getCustomProfileImageURL() {
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
        // hit /users/edit
//        ac.setUsername(username);
        return true;
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        activeCurrentActiveUser = null;
        // delete local session
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
            return true;
        } else {
            return false;
        }
    }
}
