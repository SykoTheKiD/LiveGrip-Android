package com.jaysyko.wrestlechat.forms;

import com.jaysyko.wrestlechat.R;

public class Form {

    public FormStatus reason;
    public Boolean valid;

    public Form(Boolean valid, FormStatus reason) {
        this.valid = valid;
        this.reason = reason;
    }

    public static int getSimpleMessage(FormStatus status) {
        switch (status) {
            case BLANK_FIELDS:
                return R.string.blank_username;
            case INVALID_USERNAME:
                return R.string.invalid_username;
            case VALID:
                return R.string.valid;
            case INVALID_MESSAGE:
                return R.string.message_too_short;
        }
        return R.string.valid;
    }
}
