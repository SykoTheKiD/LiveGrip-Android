package com.jaysyko.wrestlechat.auth;

import android.util.Log;

import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.utils.ImageTools;
import com.jaysyko.wrestlechat.utils.StringResources;
import com.parse.ParseException;
import com.parse.ParseUser;

public class CurrentActiveUser {
    private static CurrentActiveUser activeCurrentActiveUser;
    private String username;
    private String password;
    private String profileImageURL;

    private CurrentActiveUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static CurrentActiveUser getInstance(String username, String password) {
        if (activeCurrentActiveUser == null) {
            activeCurrentActiveUser = new CurrentActiveUser(username, password);
        }
        return activeCurrentActiveUser;
    }

    public static CurrentActiveUser getInstance() {
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            activeCurrentActiveUser = new CurrentActiveUser(currentUser.getUsername(), null);
        }
        return activeCurrentActiveUser;
    }

    public static boolean signUpUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username.toLowerCase());
        user.setPassword(password);
        try {
            user.signUp();
            CurrentActiveUser.getInstance(username, password);
            return true;
        } catch (ParseException e) {
            Log.d("Login", e.getMessage());
            return false;
        }
    }

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

    public String getUsername() {
        return username;
    }

    public boolean loginUser() {
        try {
            ParseUser.logIn(activeCurrentActiveUser.username, activeCurrentActiveUser.password);
            return true;
        } catch (ParseException e) {
            activeCurrentActiveUser = null;
            return false;
        }

    }

    public void logout() {
        activeCurrentActiveUser = null;
        ParseUser.logOut();
    }

    public boolean isLoggedIn() {
        return activeCurrentActiveUser != null;
    }

}
