package com.jaysyko.wrestlechat.auth;

import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * CreateNewUser.java
 * Creates and stores a new user into the system
 *
 * @author Jay Syko
 */
public class CreateNewUser {
    /**
     * SignUp the user into the app returns true if successful else returns false
     *
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
}
