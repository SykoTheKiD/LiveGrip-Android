package com.jaysyko.wrestlechat.forms.formValidators;

import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.FormStatus;


public class MessageValidator extends FormValidator {
    private static final int MIN_MESSAGE_LENGTH = 1;
    private static final int MAX_MESSAGE_LENGTH = 112;

    private Form form;

    public MessageValidator(String message) {
        this.form = validate(message);
    }


    private static Form isValidMessage(String message) {
        if (!(message.length() > MIN_MESSAGE_LENGTH && message.length() < MAX_MESSAGE_LENGTH)) {
            return new Form(false, FormStatus.INVALID_MESSAGE);
        }
        return new Form(true, FormStatus.VALID);
    }

    @Override
    public Form validate() {
        return this.form;
    }

    @Override
    public Form validate(String message) {
        return isValidMessage(message);
    }
}
