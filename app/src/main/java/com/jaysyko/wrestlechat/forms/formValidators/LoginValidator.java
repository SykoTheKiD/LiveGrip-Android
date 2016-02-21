package com.jaysyko.wrestlechat.forms.formValidators;

import com.jaysyko.wrestlechat.forms.Form;

/**
 *
 */
public class LoginValidator extends FormValidator {

    Form form;

    public LoginValidator(String... args) {
        this.form = validate(args);

    }

    public Form validate() {
        return form;
    }

    @Override
    protected Form validate(String... args) {
        return isNotBlank(args);
    }

}
