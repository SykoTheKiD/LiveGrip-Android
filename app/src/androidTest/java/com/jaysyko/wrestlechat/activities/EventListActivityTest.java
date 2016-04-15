package com.jaysyko.wrestlechat.activities;

import android.support.test.rule.ActivityTestRule;

import com.jaysyko.wrestlechat.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by jarushaan on 2016-04-15
 */
public class EventListActivityTest {

    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void validateEditText() {
        onView(withId(R.id.drawerLayout)).perform(swipeRight());
        onView(withId(R.id.drawer_username)).check(matches(isEnabled()));
    }
}
