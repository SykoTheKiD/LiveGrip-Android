package com.jaysyko.wrestlechat.auth;

/**
 * Created by jarushaan on 2016-02-05.
 */
public interface ActiveUser {
    boolean getCurrentUser();

    String getUsername();

    boolean loginUser();

    boolean signUpUser();

    void logout();

}
