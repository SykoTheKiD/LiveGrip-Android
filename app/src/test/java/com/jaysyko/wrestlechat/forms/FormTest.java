package com.jaysyko.wrestlechat.forms;

import com.jaysyko.wrestlechat.R;
import com.jaysyko.wrestlechat.forms.formTypes.LoginForm;
import com.jaysyko.wrestlechat.forms.formTypes.SignUpForm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Jay Syko
 */
public class FormTest {

    @Test
    public void loginValidatorValidTest() {
        Form form = new LoginForm("jaysyko", "password").validate();
        assertEquals(true, form.isValid());
        assertEquals(FormStatus.VALID, form.getReason());
        assertEquals(R.string.valid, Form.getSimpleMessage(form.getReason()));
    }

    @Test
    public void loginValidatorFailTest() {
        Form form = new LoginForm("", "password").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());

        form = new LoginForm("", "").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());

        form = new LoginForm("", "password").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());

        form = new LoginForm(null, null).validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());
        assertEquals(R.string.blank_username, Form.getSimpleMessage(form.getReason()));
    }

    @Test
    public void signUpValidatorValidTest() {
        Form form = new SignUpForm("jaysyko", "jaysyko").validate();
        assertEquals(true, form.isValid());
    }

    @Test
    public void signUpValidatorFailTest() {
        Form form = new SignUpForm("", "").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());

        form = new SignUpForm("!!syfdfdodifs!><:dasd", "password").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.SPECIAL_CHARACTERS, form.getReason());
        assertEquals(R.string.special_chars_username, Form.getSimpleMessage(form.getReason()));
    }

}
