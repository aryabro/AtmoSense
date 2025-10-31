package edu.uiuc.cs427app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.graphics.Color;
import android.widget.Button;
import android.content.res.ColorStateList;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

// This mainly manages and applies user-specific theme colors from AccountManager.

public class ThemeManager {

    private static final String TAG = "ThemeManager";
    private static final String ACCOUNT_TYPE = "edu.uiuc.cs427app";
    private static final String KEY_BACKGROUND_COLOR = "backgroundColor";
    private static final String KEY_TEXT_COLOR = "textColor";
    private static final String KEY_BUTTON_BG = "buttonBackgroundColor";
    private static final String KEY_BUTTON_TEXT = "buttonTextColor";

    //Applies the user's theme to the entire activity.
    public static void applyTheme(Activity activity) {
        String backgroundColor = getBackgroundColor(activity);
        String textColor = getTextColor(activity);
        String buttonBackground = getButtonBackgroundColor(activity);
        String buttonText = getButtonTextColor(activity);

        View rootView = activity.findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.setBackgroundColor(Color.parseColor(backgroundColor));
            applyTextColorToViews(rootView, textColor);
            applyButtonColorsToViews(rootView, buttonBackground, buttonText);
        }
    }

    //Recursively applies text color to all TextViews in a view hierarchy.
    private static void applyTextColorToViews(View view, String textColor) {
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(Color.parseColor(textColor));
        }
        
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                applyTextColorToViews(child, textColor);
            }
        }
    }

    //Recursively applies background and text colors to Buttons.
    private static void applyButtonColorsToViews(View view, String buttonBackground, String buttonText) {
        if (view instanceof Button) {
            Button b = (Button) view;
            // text
            b.setTextColor(Color.parseColor(buttonText));
            // background tint
            ColorStateList tint = ColorStateList.valueOf(Color.parseColor(buttonBackground));
            ViewCompat.setBackgroundTintList(b, tint);
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyButtonColorsToViews(group.getChildAt(i), buttonBackground, buttonText);
            }
        }
    }

    //Retrieves the current user's account from AccountManager.
    //Attempts to identify the correct account by checking the Intent for username.
    private static Account getCurrentAccount(Activity activity, AccountManager accountManager) {
        // Try to get username from Intent
        String username = activity.getIntent().getStringExtra("username");
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        
        // If we have a username, find the matching account
        if (username != null && !username.isEmpty()) {
            for (Account account : accounts) {
                if (account.name.equals(username)) {
                    return account;
                }
            }
        } else {
            Log.d(TAG,"No username found in Intent");
        }

        Log.d(TAG,"No matching account found for username: " + username);
        return null;
    }

    // Gets the user's background color from AccountManager, if not it sets white.
    public static String getBackgroundColor(Activity activity) {
        AccountManager accountManager = AccountManager.get(activity);
        Account account = getCurrentAccount(activity, accountManager);

        if (account != null) {
            String backgroundColor = accountManager.getUserData(account, KEY_BACKGROUND_COLOR);
            if (isValidColor(backgroundColor)) {
                return backgroundColor;
            }
        }
        return "#FFFFFF"; // default to white
    }

    // Gets the user's text color from AccountManager, if not it sets black.
    public static String getTextColor(Activity activity) {
        AccountManager accountManager = AccountManager.get(activity);
        Account account = getCurrentAccount(activity, accountManager);

        if (account != null) {
            String textColor = accountManager.getUserData(account, KEY_TEXT_COLOR);
            if (isValidColor(textColor)) {
                return textColor;
            }
        }
        return "#000000"; // default to black
    }

    // Gets the user's button background color from AccountManager, if not it sets black.
    public static String getButtonBackgroundColor(Activity activity) {
        AccountManager accountManager = AccountManager.get(activity);
        Account account = getCurrentAccount(activity, accountManager);

        if (account != null) {
            String stored = accountManager.getUserData(account, KEY_BUTTON_BG);
            if (isValidColor(stored)) return stored;
        }
        return "#000000"; // default to black
    }

    // Gets the user's button text color from AccountManager, if not it sets white.
    public static String getButtonTextColor(Activity activity) {
        AccountManager accountManager = AccountManager.get(activity);
        Account account = getCurrentAccount(activity, accountManager);

        if (account != null) {
            String stored = accountManager.getUserData(account, KEY_BUTTON_TEXT);
            if (isValidColor(stored)) return stored;
        }
        return "#FFFFFF"; // default to white
    }

    //Checks if a string is a valid hex color code.
    private static boolean isValidColor(String colorString) {
        if (colorString == null || colorString.isEmpty()) {
            return false;
        }
        try {
            Color.parseColor(colorString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
