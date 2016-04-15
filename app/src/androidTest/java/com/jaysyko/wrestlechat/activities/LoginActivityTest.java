package com.jaysyko.wrestlechat.activities;

/**
 * Created by jarushaan on 2016-04-15
 */

import android.support.test.rule.ActivityTestRule;

import com.jaysyko.wrestlechat.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

// Tests for MainActivity
public class LoginActivityTest {

    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    // Looks for an EditText with id = "R.id.etInput"
    // Types the text "Hello" into the EditText
    // Verifies the EditText has text "Hello"
    @Test
    public void validateEditText() {
        onView(withId(R.id.sign_up_text_view)).perform(click());
        onView(withId(R.id.sign_up_text_view)).check(matches(isDisplayed()));
    }
}