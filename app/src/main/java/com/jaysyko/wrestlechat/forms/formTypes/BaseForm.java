package com.jaysyko.wrestlechat.forms.formTypes;

import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.FormStatus;


public abstract class BaseForm {
    public final Form isNotBlank(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return new Form(false, FormStatus.BLANK_FIELDS);
            }
        }
        return new Form(true, FormStatus.VALID);
    }

    public abstract Form validate();

    protected Form validate(String... args) {
        return null;
    }

    protected Form validate(String string) {
        return null;
    }

    protected Form validate(String str1, String str2) {
        return null;
    }
}
