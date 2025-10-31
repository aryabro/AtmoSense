package edu.uiuc.cs427app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// Base class for activities to automatically apply the user's saved theme.
// Inherit from this class to have UI colors set on creation.
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call the super class implementation first and Apply the custom theme managed by
        // ThemeManager to this activity.
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
    }
}
