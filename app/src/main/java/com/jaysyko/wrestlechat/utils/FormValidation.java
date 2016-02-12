package com.jaysyko.wrestlechat.utils;

public class FormValidation {

    private static final int MIN_MESSAGE_LENGTH = 1;
    private static final int MAX_MESSAGE_LENGTH = 112;
    private static final int MAX_USERNAME_LENGTH = 10;

    public static boolean formIsClean(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidMessage(String message) {
        return message.length() > MIN_MESSAGE_LENGTH && message.length() < MAX_MESSAGE_LENGTH;
    }

    public static boolean isValidUsername(String username) {
        return username.length() < MAX_USERNAME_LENGTH;
    }
}
