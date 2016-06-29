package com.jaysyko.wrestlechat.sessionManager;

import com.jaysyko.wrestlechat.models.User;

/**
 * Session.java
 * A session object that holds an active session for a logged in user
 *
 * @author Jay Syko
 */
class Session {

    private static Session instance;
    private User currentUser;

    private Session() {
    }

    /**
     * Get an instance of the current session
     *
     * @return Session
     */
    static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }


    /**
     * Returns the current user associated with the current session
     *
     * @return User currentUser
     */
    User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current active user associated with this session
     *
     * @param user User
     */
    void setCurrentUser(User user) {
        if (currentUser == null) {
            currentUser = user;
        }
    }

    /**
     * Destroys the session object once user logs out
     */
    void endSession() {
        instance = null;
        currentUser = null;
    }

}
