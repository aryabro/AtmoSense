package edu.uiuc.cs427app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class Utils {

    public static void clearAccounts(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(LoginActivity.ACCOUNT_TYPE);
        for (Account account : accounts) {
            accountManager.removeAccount(account, null, null, null);
        }
    }

    public static Matcher<View> hasNoChildren() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return item instanceof ViewGroup && ((ViewGroup) item).getChildCount() == 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has no children");
            }
        };
    }
}
