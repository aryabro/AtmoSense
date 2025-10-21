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
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private LinearLayout locationContainer;
    private ArrayList<String> cityList;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CityListPrefs";
    private static final String CITIES_KEY = "cities";

    private void showAddLocationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add a New City");
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Enter city name");
        builder.setView(input);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String cityName = input.getText().toString().trim();
            if (!cityName.isEmpty()) {
                if (cityList.contains(cityName)) {
                    android.widget.Toast
                            .makeText(this, "City already exists in the list!", android.widget.Toast.LENGTH_SHORT)
                            .show();
                } else {
                    addCityToList(cityName);
                }
            } else {
                android.widget.Toast.makeText(this, "Please enter a city name!", android.widget.Toast.LENGTH_SHORT)
                        .show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
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
}
