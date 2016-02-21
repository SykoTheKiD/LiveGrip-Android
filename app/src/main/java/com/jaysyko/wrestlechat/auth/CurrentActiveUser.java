package com.jaysyko.wrestlechat.auth;

import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.StringResources;
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
     * Return's a new instance of a current logged in user
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
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser getInstance() {
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            activeCurrentActiveUser = new CurrentActiveUser(currentUser.getUsername(), null);
        }
        return activeCurrentActiveUser;
    }

    /**
     * SignUp the user into the app returns true if successful else returns false
     * @param username String
     * @param password String
     * @return boolean
     */
    public static boolean signUpUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username.toLowerCase());
        user.setPassword(password);
        try {
            user.signUp();
            CurrentActiveUser.getInstance(username, password);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Get the user defined imageUrl else return an generic image
     * @return imageUrl: String
     */
    public String getCustomProfileImageURL() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String userImage = currentUser.getString(User.IMG_ID);
        if (userImage != null) {
            activeCurrentActiveUser.profileImageURL = StringResources.IMGUR_LINK.concat(userImage);
        } else {
            activeCurrentActiveUser.profileImageURL = ImageTools.defaultProfileImage(activeCurrentActiveUser.getUsername());
        }
        return activeCurrentActiveUser.profileImageURL;
    }

    /**
     * Return the current user's username
     * @return username: String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Login the user; returns true if successful else returns false
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

}
