package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LogoutTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    private String user1;
    private String pass1;
    private String user2;
    private String pass2;

    @Before
    public void setUp() {
        // Clear any accounts before each test
        Utils.clearAccounts(ApplicationProvider.getApplicationContext());

        // Initialize user credentials
        user1 = "testUser1";
        pass1 = "password123";
        user2 = "testUser2";
        pass2 = "password456";
    }

    @Test
    public void testLogoutButton() {
        // Sign up a new user using credentials from setUp
        onView(withId(R.id.username)).perform(typeText(user1), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(pass1), closeSoftKeyboard());
        onView(withId(R.id.CreateAccountButton)).perform(click());

        // Wait for MainActivity to appear by checking for the logout button
        onView(withId(R.id.buttonLogout)).check(matches(isDisplayed()));

        // Click the logout button
        onView(withId(R.id.buttonLogout)).perform(click());

        // Verify that we are back on the LoginActivity
        onView(withId(R.id.login_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testCityListClearedAfterLogout() throws InterruptedException {
        // Sign up user 1 and add a city
        onView(withId(R.id.username)).perform(typeText(user1), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(pass1), closeSoftKeyboard());
        onView(withId(R.id.CreateAccountButton)).perform(click());

        // Wait for MainActivity and add a city
        onView(withId(R.id.buttonAddLocation)).perform(click());

        // Correctly target the EditText by its hint text
        onView(withHint("Enter city name")).perform(typeText("Chicago"), closeSoftKeyboard());
        onView(withText("Add")).perform(click());

        // Wait for async validation to complete. In a real app, use IdlingResource here.
        Thread.sleep(3000);

        // Dismiss the success dialog before proceeding
        onView(withText("OK")).perform(click());

        // Logout
        onView(withId(R.id.buttonLogout)).perform(click());

        // Sign up user 2
        onView(withId(R.id.username)).perform(typeText(user2), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(pass2), closeSoftKeyboard());
        onView(withId(R.id.CreateAccountButton)).perform(click());

        // Verify that the city list for user 2 is empty
        onView(withId(R.id.locationContainer)).check(matches(Utils.hasNoChildren()));
    }
}
