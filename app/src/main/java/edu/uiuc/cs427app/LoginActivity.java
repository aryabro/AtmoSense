
package edu.uiuc.cs427app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, themeDescriptionEditText;
    private Button loginButton;
    private ConstraintLayout loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        themeDescriptionEditText = findViewById(R.id.theme_description);
        loginButton = findViewById(R.id.login);
        loginLayout = findViewById(R.id.login_layout);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String themeDescription = themeDescriptionEditText.getText().toString();
                if (!themeDescription.isEmpty()) {
                    generateTheme(themeDescription);
                } else {
                    applyDefaultTheme();
                }
            }
        });
    }

    private void generateTheme(String description) {
        String apiKey = BuildConfig.GEMINI_API_KEY;
        if (apiKey == null || apiKey.isEmpty()) {
            Toast.makeText(this, "API key is not set. Please add it to your local.properties file.", Toast.LENGTH_LONG).show();
            applyDefaultTheme();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                // Create the request body
                String jsonBody = "{" +
                        "\"contents\": [{" +
                        "\"parts\":[{" +
                        "\"text\": \"Generate a UI theme based on the following description: " + description + ". " +
                        "Provide a JSON object with 'backgroundColor' and 'textColor' in hex format.\"" +
                        "}]" +
                        "}]" +
                        "}";

                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Read the response
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                String resultText = parts.getJSONObject(0).getString("text");

                // Clean and parse the theme from the result
                String jsonString = resultText.replace("```json", "").replace("```", "").trim();
                JSONObject theme = new JSONObject(jsonString);
                String backgroundColor = theme.getString("backgroundColor");
                String textColor = theme.getString("textColor");

                handler.post(() -> applyTheme(backgroundColor, textColor));

            } catch (Exception e) {
                Log.e("GeminiAPI", "API call failed", e);
                handler.post(() -> {
                    Toast.makeText(LoginActivity.this, "Failed to generate theme. Applying default.", Toast.LENGTH_LONG).show();
                    applyDefaultTheme();
                });
            }
        });
    }

    private void applyTheme(String backgroundColor, String textColor) {
        loginLayout.setBackgroundColor(Color.parseColor(backgroundColor));
        usernameEditText.setTextColor(Color.parseColor(textColor));
        passwordEditText.setTextColor(Color.parseColor(textColor));
        themeDescriptionEditText.setTextColor(Color.parseColor(textColor));
        loginButton.setBackgroundColor(Color.parseColor(textColor));
        loginButton.setTextColor(Color.parseColor(backgroundColor));
    }

    private void applyDefaultTheme() {
        // Default theme in case of API failure or empty description
        loginLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        usernameEditText.setTextColor(Color.parseColor("#000000"));
        passwordEditText.setTextColor(Color.parseColor("#000000"));
        themeDescriptionEditText.setTextColor(Color.parseColor("#000000"));
        loginButton.setBackgroundColor(Color.parseColor("#6200EE"));
        loginButton.setTextColor(Color.parseColor("#FFFFFF"));
    }
}
