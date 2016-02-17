package com.jaysyko.wrestlechat.forms;

public class FormValidation {

    private static final int MIN_MESSAGE_LENGTH = 1;
    private static final int MAX_MESSAGE_LENGTH = 112;
    private static final int MAX_USERNAME_LENGTH = 10;

    private static Form isNotBlank(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return new Form(false, FormStatus.BLANK_FIELDS);
            }
        }
        return new Form(true, FormStatus.VALID);
    }

    private static Form isValidMessage(String message) {
        if (!(message.length() > MIN_MESSAGE_LENGTH && message.length() < MAX_MESSAGE_LENGTH)) {
            return new Form(false, FormStatus.INVALID_MESSAGE);
        }
        return new Form(true, FormStatus.VALID);
    }

    private static Form isValidUsername(String username) {
        if (!(username.length() < MAX_USERNAME_LENGTH)) {
            return new Form(false, FormStatus.INVALID_USERNAME);
        }
        return new Form(true, FormStatus.VALID);
    }

    public static Form validateLogin(String... args) {
        return isNotBlank(args);
    }

    public static Form validateSignUp(String username, String password) {
        Form ret = isNotBlank(username, password);
        if (!(ret.isValid())) {
            return ret;
        }
        ret = isValidUsername(username);
        if (!(ret.isValid())) {
            return ret;
        }
        return ret;
    }

    public static Form validateMessage(String message) {
        return isValidMessage(message);
    }
}
