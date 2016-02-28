package com.jaysyko.wrestlechat.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * User.java
 * Model for the User object in the database
 *
 * @author Jay Syko
 */

@ParseClassName(User.TABLE_NAME)
public class User extends ParseObject {
    public static final String TABLE_NAME = "User";
    public static final String IMG_ID = "imgID";
}
