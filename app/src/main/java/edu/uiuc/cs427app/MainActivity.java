package edu.uiuc.cs427app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import edu.uiuc.cs427app.databinding.ActivityMainBinding;

import android.widget.Button;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private LinearLayout locationContainer;
    private ArrayList<String> cityList;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CityListPrefs";
    private static final String CITIES_KEY = "cities";
    private android.app.ProgressDialog progressDialog;

    private void showAddLocationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add a New City");
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Enter city name");
        builder.setView(input);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String cityName = input.getText().toString().trim();
            if (!cityName.isEmpty()) {
                // Basic input validation
                if (cityName.length() < 2) {
                    showInputErrorDialog("City name must be at least 2 characters long!");
                } else if (cityName.length() > 100) {
                    showInputErrorDialog("City name is too long! Please keep it under 100 characters.");
                } else if (isCityAlreadyInList(cityName)) {
                    showInputErrorDialog("This city already exists in your list!");
                } else if (containsNumbers(cityName)) {
                    showInputErrorDialog("City names should not contain numbers. Please enter a valid city name.");
                } else {
                    validateAndAddCity(cityName);
                }
            } else {
                showInputErrorDialog("Please enter a city name!");
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showInputErrorDialog(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("⚠️ Input Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            // Reopen the add city dialog
            showAddLocationDialog();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    private boolean containsNumbers(String text) {
        return text.matches(".*\\d.*");
    }

    private boolean isCityAlreadyInList(String cityName) {
        // Get the normalized version of the input city name
        String normalizedInput = LocalCityValidator.getNormalizedCityName(cityName);
        if (normalizedInput == null) {
            return false; // If it's not a valid city, it can't be a duplicate
        }

        // Check if any city in the list matches the normalized input
        for (String existingCity : cityList) {
            if (existingCity.equals(normalizedInput)) {
                return true;
            }
        }
        return false;
    }

    private void validateAndAddCity(String cityName) {
        // Show progress dialog
        progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setMessage("Validating city...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Simulate a brief validation delay for better UX
        new android.os.Handler().postDelayed(() -> {
            // Dismiss progress dialog
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            // Validate city using the LocalCityValidator
            boolean isValid = LocalCityValidator.isValidCity(cityName);
            String message = LocalCityValidator.getValidationMessage(cityName);

            if (isValid) {
                // Get the normalized city name to store in the list
                String normalizedCityName = LocalCityValidator.getNormalizedCityName(cityName);
                addCityToList(normalizedCityName);
                showSuccessDialog(normalizedCityName);
            } else {
                showErrorDialog(cityName, message);
            }
        }, 500); // 500ms delay to show the progress dialog
    }

    private void showSuccessDialog(String cityName) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("✅ City Added Successfully!");
        builder.setMessage("The city \"" + cityName + "\" has been added to your list.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.show();
    }

    private void showErrorDialog(String cityName, String errorMessage) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("❌ City Not Found");
        builder.setMessage(errorMessage);

        // Get suggestions for similar cities
        List<String> suggestions = LocalCityValidator.getSimilarCities(cityName, 3);

        if (!suggestions.isEmpty()) {
            builder.setMessage(errorMessage + "\n\nWould you like to add one of these cities instead?");

            // Create buttons for each suggestion
            for (String suggestion : suggestions) {
                builder.setNeutralButton(suggestion, (dialog, which) -> {
                    // Use the normalized city name from the suggestion
                    String normalizedSuggestion = LocalCityValidator.getNormalizedCityName(suggestion);
                    if (normalizedSuggestion != null) {
                        addCityToList(normalizedSuggestion);
                        showSuccessDialog(normalizedSuggestion);
                    }
                });
            }
        }

        builder.setPositiveButton("Try Again", (dialog, which) -> {
            // Reopen the add city dialog
            showAddLocationDialog();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    private void addCityToList(String cityName) {
        // Add to city list
        cityList.add(cityName);

        // Add to UI
        addCityToUI(cityName);

        // Save to preferences
        saveCityList();
    }

    private void removeCityFromList(String cityName, LinearLayout row) {
        // Remove from city list
        cityList.remove(cityName);

        // Remove from UI
        locationContainer.removeView(row);

        // Save to preferences
        saveCityList();
    }

    private void saveCityList() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> citySet = new HashSet<>(cityList);
        editor.putStringSet(CITIES_KEY, citySet);
        editor.apply();
    }

    private void loadCityList() {
        Set<String> citySet = sharedPreferences.getStringSet(CITIES_KEY, new HashSet<>());
        cityList = new ArrayList<>(citySet);

        // Rebuild UI with loaded cities
        for (String cityName : cityList) {
            addCityToUI(cityName);
        }
    }

    private void addCityToUI(String cityName) {
        // Create row layout
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setTag(cityName); // Tag for easy identification

        // City name text view
        android.widget.TextView cityText = new android.widget.TextView(this);
        cityText.setText(cityName);
        cityText.setTextSize(18);
        cityText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        // Details button
        android.widget.Button detailsButton = new android.widget.Button(this);
        detailsButton.setText("Show Details");
        detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("city", cityName);
            startActivity(intent);
        });

        // Remove button
        android.widget.Button removeButton = new android.widget.Button(this);
        removeButton.setText("Remove");
        removeButton.setOnClickListener(v -> removeCityFromList(cityName, row));

        row.addView(cityText);
        row.addView(detailsButton);
        row.addView(removeButton);
        locationContainer.addView(row);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences and city list
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        cityList = new ArrayList<>();

        // Initializing the UI components
        locationContainer = findViewById(R.id.locationContainer);
        Button buttonNew = findViewById(R.id.buttonAddLocation);

        buttonNew.setOnClickListener(this);

        // Load saved cities
        loadCityList();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonAddLocation) {
            showAddLocationDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up progress dialog to prevent memory leaks
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
