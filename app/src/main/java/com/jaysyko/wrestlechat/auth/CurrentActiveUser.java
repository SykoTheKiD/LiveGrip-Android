package com.jaysyko.wrestlechat.auth;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;

public class CurrentActiveUser implements ActiveUser {
    public static final String USERNAME_KEY = "username";
    private static CurrentActiveUser activeCurrentActiveUser;
    private String username;
    private String password;

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
        return activeCurrentActiveUser;
    }

    public static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    public void setPassword(String password) {
        if (activeCurrentActiveUser.password == null) {
            activeCurrentActiveUser.password = password;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (activeCurrentActiveUser.username == null) {
            activeCurrentActiveUser.username = username;
        }
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
        ParseUser.logOut();
    }

    public boolean getCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser != null;
    }


}
