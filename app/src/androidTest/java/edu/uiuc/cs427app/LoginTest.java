package edu.uiuc.cs427app;

// Static imports for Espresso
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

// Static imports for Espresso Intents - REQUIRED
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

// Android testing
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

// Android account management
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

// JUnit
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    private AccountManager accountManager;
    private Context context;
    private String testUsername;
    private final String testPassword = "testpassword123";

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        Intents.init();

        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        accountManager = AccountManager.get(context);

        // Use a unique username to avoid conflicts from previous failed test runs
        testUsername = "testuser_" + System.currentTimeMillis();

        // Remove any lingering accounts from previous runs before adding the new one
        cleanupTestAccount();

        // Add a test account to the AccountManager for the test to use
        Account testAccount = new Account(testUsername, LoginActivity.ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(testAccount, testPassword, null);
    }

    @After
    public void tearDown() {
        cleanupTestAccount();
        Intents.release();
    }

    /**
     * Helper method to find and remove the test account to ensure test isolation.
     */
    private void cleanupTestAccount() {
        Account[] accounts = accountManager.getAccountsByType(LoginActivity.ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equals(testUsername)) {
                accountManager.removeAccountExplicitly(account);
            }
        }
    }

    @Test
    public void checkUserLogin() throws InterruptedException {
        // a. Type a test username into R.id.username
        onView(withId(R.id.username)).perform(typeText(testUsername));

        // b. Sleep for 1000ms
        Thread.sleep(1000);

        // c. Close the soft keyboard
        closeSoftKeyboard();

        // d. Sleep for 1000ms
        Thread.sleep(1000);

        // e. Type a test password into R.id.password
        onView(withId(R.id.password)).perform(typeText(testPassword));

        // f. Sleep for 1000ms
        Thread.sleep(1000);

        // g. Close the soft keyboard
        closeSoftKeyboard();

        // h. Sleep for 1000ms
        Thread.sleep(1000);

        // i. Click the login button (R.id.login)
        onView(withId(R.id.login)).perform(click());

        // j. Sleep for 1000ms
        Thread.sleep(1000);

        // Assertion: Verify that MainActivity was launched
        intended(hasComponent(MainActivity.class.getName()));
    }
}