package com.jaysyko.wrestlechat.forms.formTypes;

import com.jaysyko.wrestlechat.forms.Form;

/**
 *
 */
public class LoginForm extends BaseForm {

    Form form;

    public LoginForm(String... args) {
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
