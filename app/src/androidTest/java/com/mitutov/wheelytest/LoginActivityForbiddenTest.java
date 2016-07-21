package com.mitutov.wheelytest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mitutov.wheelytest.ui.presenter.LoginActivityPresenterImpl;
import com.mitutov.wheelytest.ui.view.LoginActivity;
import com.mitutov.wheelytest.util.WheelyMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Alexey Mitutov on 19.07.16.
 *
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityForbiddenTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    LoginActivityPresenterImpl presenter;
    LoginActivity activity;

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
        presenter = activity.presenter;
    }

    @Test
    public void test_Forbidden() {
        onView(withId(R.id.userNameEditText)).perform(typeText("forbidden"));
        onView(withId(R.id.passwordEditText)).perform(typeText("forbidden"), closeSoftKeyboard());
        onView(withId(R.id.btnConnect)).perform(click());
        onView(withText(R.string.toast_forbidden)).inRoot(WheelyMatchers.isToast()).check(matches(isDisplayed()));
    }
}
