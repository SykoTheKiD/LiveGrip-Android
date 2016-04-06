package com.jaysyko.wrestlechat.forms;

import com.jaysyko.wrestlechat.forms.formValidators.LoginValidator;
import com.jaysyko.wrestlechat.forms.formValidators.SignUpValidator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Jay Syko
 */
public class FormTest {

    @Test
    public void loginValidatorValidTest() {
        Form form = new LoginValidator("jaysyko", "password").validate();
        assertEquals(true, form.isValid());
        assertEquals(FormStatus.VALID, form.getReason());
    }

    @Test
    public void loginValidatorFailTest() {
        Form form = new LoginValidator("", "password").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());

        form = new LoginValidator("", "").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());

        form = new LoginValidator("", "password").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());

        form = new LoginValidator(null, null).validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());
    }

    @Test
    public void signUpValidatorValidTest() {
        Form form = new SignUpValidator("jaysyko", "jaysyko").validate();
        assertEquals(true, form.isValid());
    }

    @Test
    public void signUpValidatorFailTest() {
        Form form = new SignUpValidator("", "").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.BLANK_FIELDS, form.getReason());

        form = new SignUpValidator("!!syfdfdodifs!><:dasd", "password").validate();
        assertEquals(false, form.isValid());
        assertEquals(FormStatus.SPECIAL_CHARACTERS, form.getReason());
    }
}
