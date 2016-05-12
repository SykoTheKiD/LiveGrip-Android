package com.jaysyko.wrestlechat.forms.formTypes;

import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.FormStatus;


public class MessagingForm extends BaseForm {
    private static final int MIN_MESSAGE_LENGTH = 1;
    private static final int MAX_MESSAGE_LENGTH = 230;

    private Form form;

    public MessagingForm(String message) {
        this.form = validate(message);
    }


    private static Form isValidMessage(String message) {
        if(message.length() < MIN_MESSAGE_LENGTH){
            return new Form(false, FormStatus.MESSAGE_TOO_SHORT);
        }else if(message.length() > MAX_MESSAGE_LENGTH){
            return new Form(false, FormStatus.MESSAGE_TOO_LONG);
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
