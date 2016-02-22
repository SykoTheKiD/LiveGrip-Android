package com.jaysyko.wrestlechat.forms;

import com.jaysyko.wrestlechat.R;

/**
 * Form.java
 * Form object used on all forms in the app
 *
 * @author Jay Syko
 */
public class Form {

    private FormStatus reason;
    private Boolean valid;

    public Form(Boolean valid, FormStatus reason) {
        this.valid = valid;
        this.reason = reason;
    }

    /**
     * Returns a user-friendly message from strings.xml from a form status enum
     * @param status FormStatus
     * @return Integer
     */
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
            case SPECIAL_CHARACTERS:
                return R.string.special_chars_username;
        }
        return R.string.valid;
    }

    /**
     * Get the reason the form is invalid
     * @return FormStatus
     */
    public FormStatus getReason() {
        return this.reason;
    }

    /**
     * Returns if form is valid or not
     * @return valid Boolean
     */
    public Boolean isValid() {
        return this.valid;
    }
}
