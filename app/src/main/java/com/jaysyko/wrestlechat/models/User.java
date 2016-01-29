package com.jaysyko.wrestlechat.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.math.BigInteger;
import java.security.MessageDigest;

@ParseClassName(User.PARSE_USER_TABLE)
public class User extends ParseObject {
    public static final String PARSE_USER_TABLE = "Users";
    public static final String USERNAME_KEY = "username";


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
}