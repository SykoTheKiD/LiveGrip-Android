package com.jaysyko.wrestlechat.forms.formTypes;

import com.jaysyko.wrestlechat.forms.Form;
import com.jaysyko.wrestlechat.forms.FormStatus;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class SignUpForm extends BaseForm {

    private static final int MAX_USERNAME_LENGTH = 25;
    private Form form;

    public SignUpForm(String username, String password) {
        this.form = validate(username, password);
    }

    public Form validate() {
        return this.form;
    }

    protected Form validate(String username, String password) {
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

    private Form isValidUsername(String username) {
        if (username.length() > MAX_USERNAME_LENGTH) {
            return new Form(false, FormStatus.INVALID_USERNAME);
        }
        String patternToMatch = "[\\\\!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~]+";
        Pattern p = Pattern.compile(patternToMatch);
        Matcher m = p.matcher(username);
        if (m.find() || username.contains(StringResources.BLANK_SPACE)) {
            return new Form(false, FormStatus.SPECIAL_CHARACTERS);
        }
        return new Form(true, FormStatus.VALID);
    }
}
