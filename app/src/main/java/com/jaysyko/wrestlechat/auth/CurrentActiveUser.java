package com.jaysyko.wrestlechat.auth;

import com.jaysyko.wrestlechat.models.User;

/**
 * CurrentActiveUser.java
 * A single instance for the currently logged in user
 *
 * @author Jay Syko
 */
public class CurrentActiveUser {
    private static CurrentActiveUser activeUser;
    private User user;

    private CurrentActiveUser(User user) {
        this.user = user;
    }


    public static CurrentActiveUser getInstance(){
        return activeUser;
    }

    public static void newInstance(User user){
        if(activeUser == null){
            activeUser = new CurrentActiveUser(user);
        }
    }

    public User getCurrentUser(){
        return user;
    }

    public void destroySession(){
        activeUser = null;
    }
}
