package com.jaysyko.wrestlechat.auth;

import com.jaysyko.wrestlechat.models.User;

/**
 * CurrentActiveUser.java
 * A single instance for the currently logged in user
 *
 * @author Jay Syko
 */
public class CurrentActiveUser {
    private static CurrentActiveUser activeUser = new CurrentActiveUser(null);
    private User user;

    private CurrentActiveUser(User user) {
        this.user = user;
    }


    public static CurrentActiveUser getInstance(){
        return activeUser;
    }

    public static void setActiveUser(User user){
        if(activeUser.user == null){
            activeUser.user = user;
        }
    }

    public User getCurrentUser(){
        return user;
    }

    public void destroySession(){
        user = null;
    }
}
