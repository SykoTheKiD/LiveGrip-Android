package com.jaysyko.wrestlechat.auth;

import android.util.Log;

import com.jaysyko.wrestlechat.utils.Resources;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;

public class CurrentActiveUser implements ActiveUser {
    public static final String USERNAME_KEY = "username";
    private static final String IMG_ID = "imgID";
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

    public String getProfileImage() {
        String customProfileImageURL = activeCurrentActiveUser.getCustomProfileImageURL();
        if (customProfileImageURL != null) {
            return customProfileImageURL;
        } else {
            String userId = activeCurrentActiveUser.getUsername();
            String hex = "";
            try {
                final MessageDigest digest = MessageDigest.getInstance("MD5");
                final byte[] hash = digest.digest(userId.getBytes());
                final BigInteger bigInt = new BigInteger(hash);
                hex = bigInt.abs().toString(16);
            } catch (Exception e) {
                Log.d("Profile Image", "Default profile image generator error");
            }
            return "http://www.gravatar.com/avatar/".concat(hex).concat("?d=identicon");
        }
    }

    private String getCustomProfileImageURL() {
        activeCurrentActiveUser.profileImageURL = Resources.IMGUR_LINK.concat(ParseUser.getCurrentUser().get(IMG_ID).toString());
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
            return false;
        }

    }

    public boolean signUpUser() {
        ParseUser user = new ParseUser();
        user.setUsername(activeCurrentActiveUser.username);
        user.setPassword(activeCurrentActiveUser.password);
        try {
            user.signUp();
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public void logout() {
        activeCurrentActiveUser = null;
        ParseUser.logOut();
    }

    public boolean getCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser != null;
    }


}
