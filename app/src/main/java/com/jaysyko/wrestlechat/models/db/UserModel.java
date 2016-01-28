package com.jaysyko.wrestlechat.models.db;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by jarushaan on 2016-01-27.
 */
public class UserModel {
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
