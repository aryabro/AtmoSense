package edu.uiuc.cs427app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class WeatherInsightsActivity extends BaseActivity {

    private static final String TAG = "WeatherInsights";

    // Weather data from intent
    private String cityName;
    private double temperature;
    private int humidity;
    private String condition;
    private String weatherMain;
    private double windSpeed;
    private double windDeg;

    // UI components
    private TextView titleView;
    private TextView statusView;
    private TextView errorView;
    private ProgressBar progressBar;
    private LinearLayout questionsContainer;
    private TextView answerLabel;
    private TextView answerText;
    private View answerScrollView;

    // API client
    private GeminiApiClient geminiClient;

    // State
    private List<String> generatedQuestions;
    private boolean isLoadingAnswer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_insights);

        // Get weather data from Intent
        cityName = getIntent().getStringExtra("city");
        temperature = getIntent().getDoubleExtra("temperature", 0.0);
        humidity = getIntent().getIntExtra("humidity", 0);
        condition = getIntent().getStringExtra("condition");
        weatherMain = getIntent().getStringExtra("weatherMain");
        windSpeed = getIntent().getDoubleExtra("windSpeed", 0.0);
        windDeg = getIntent().getDoubleExtra("windDeg", 0.0);

        // Initialize UI
        titleView = findViewById(R.id.insightsTitle);
        statusView = findViewById(R.id.insightsStatus);
        errorView = findViewById(R.id.insightsError);
        progressBar = findViewById(R.id.insightsProgressBar);
        questionsContainer = findViewById(R.id.questionsContainer);
        answerLabel = findViewById(R.id.answerLabel);
        answerText = findViewById(R.id.answerText);
        answerScrollView = findViewById(R.id.answerScrollView);

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Weather Insights - " + cityName);
        }

        // Initialize Gemini client
        String apiKey = BuildConfig.GEMINI_API_KEY;
        if (apiKey != null) {
            apiKey = apiKey.replace("\"", "").trim();
        }
        geminiClient = new GeminiApiClient(apiKey);

        // Start generating questions
        generateQuestions();
    }

    /**
     * First LLM call: Generate weather-related questions
     */
    private void generateQuestions() {
        showLoading(true, "Generating weather insights...");

        String weatherSummary = buildWeatherSummary();
        String prompt = "Today's weather in " + cityName + ": " + weatherSummary + ". "
                + "Generate exactly 3 practical, context-specific questions that a user might ask "
                + "to help them plan their day based on this weather. "
                + "Return ONLY a JSON array in this exact format: [\"question1\", \"question2\", \"question3\"]. "
                + "Do not include any other text, explanation, or markdown code blocks.";

        Log.d(TAG, "Question generation prompt: " + prompt);

        geminiClient.generateContent(prompt, new GeminiApiClient.GeminiCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "Questions response: " + response);
                parseAndDisplayQuestions(response);
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "Failed to generate questions: " + error);
                showError("Failed to generate questions: " + error);
            }
        });
    }

    /**
     * Builds a human-readable weather summary
     */
    private String buildWeatherSummary() {
        String windDir = getWindDirection(windDeg);
        return String.format("Temperature %.1f°C, %s, Humidity %d%%, Wind %.1f m/s %s",
                temperature, condition, humidity, windSpeed, windDir);
    }

    /**
     * Converts wind degrees to compass direction
     */
    private String getWindDirection(double degrees) {
        String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = (int) Math.round(degrees / 22.5) % 16;
        return directions[index];
    }

    /**
     * Parses the LLM response and displays questions as buttons
     */
    private void parseAndDisplayQuestions(String response) {
        try {
            // Extract JSON array from response (handle markdown code blocks)
            String jsonStr = response.trim();
            int startIdx = jsonStr.indexOf('[');
            int endIdx = jsonStr.lastIndexOf(']');

            if (startIdx == -1 || endIdx == -1) {
                showError("Invalid response format from LLM");
                return;
            }

            jsonStr = jsonStr.substring(startIdx, endIdx + 1);
            JSONArray questionsArray = new JSONArray(jsonStr);

            generatedQuestions = new ArrayList<>();
            for (int i = 0; i < questionsArray.length(); i++) {
                generatedQuestions.add(questionsArray.getString(i));
            }

            Log.d(TAG, "Parsed " + generatedQuestions.size() + " questions");
            displayQuestionButtons();

        } catch (Exception e) {
            Log.e(TAG, "Failed to parse questions", e);
            showError("Failed to parse questions: " + e.getMessage());
        }
    }

    /**
     * Creates and displays button for each question
     */
    private void displayQuestionButtons() {
        showLoading(false, null);
        questionsContainer.removeAllViews();
        questionsContainer.setVisibility(View.VISIBLE);

        for (int i = 0; i < generatedQuestions.size(); i++) {
            final String question = generatedQuestions.get(i);
            final int questionIndex = i;

            Button questionButton = new Button(this);
            questionButton.setText(question);
            questionButton.setAllCaps(false);
            questionButton.setTextSize(14);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            questionButton.setLayoutParams(params);

            questionButton.setOnClickListener(v -> {
                if (!isLoadingAnswer) {
                    generateAnswer(question);
                }
            });

            questionsContainer.addView(questionButton);
        }
    }

    /**
     * Second LLM call: Generate answer for selected question
     */
    private void generateAnswer(String question) {
        isLoadingAnswer = true;
        showLoading(true, "Generating answer...");
        disableQuestionButtons(true);

        // Hide previous answer
        answerLabel.setVisibility(View.GONE);
        answerScrollView.setVisibility(View.GONE);

        String weatherSummary = buildWeatherSummary();
        String prompt = "Weather context: " + cityName + " - " + weatherSummary + ". "
                + "User question: '" + question + "'. "
                + "Provide a helpful, concise answer (2-4 sentences) based on the weather conditions. "
                + "Give practical advice that directly addresses the question.";

        Log.d(TAG, "Answer generation prompt: " + prompt);

        geminiClient.generateContent(prompt, new GeminiApiClient.GeminiCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "Answer response: " + response);
                displayAnswer(response.trim());
                isLoadingAnswer = false;
                disableQuestionButtons(false);
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "Failed to generate answer: " + error);
                showError("Failed to generate answer: " + error);
                isLoadingAnswer = false;
                disableQuestionButtons(false);
            }
        });
    }

    /**
     * Displays the generated answer
     */
    private void displayAnswer(String answer) {
        showLoading(false, null);

        answerText.setText(answer);
        answerLabel.setVisibility(View.VISIBLE);
        answerScrollView.setVisibility(View.VISIBLE);

        // Scroll to answer
        answerScrollView.post(() -> {
            answerScrollView.requestFocus();
        });
    }

    /**
     * Enables/disables all question buttons
     */
    private void disableQuestionButtons(boolean disable) {
        for (int i = 0; i < questionsContainer.getChildCount(); i++) {
            View child = questionsContainer.getChildAt(i);
            if (child instanceof Button) {
                child.setEnabled(!disable);
            }
        }
    }

    /**
     * Shows/hides loading indicator
     */
    private void showLoading(boolean show, String message) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        if (message != null && !message.isEmpty()) {
            statusView.setText(message);
            statusView.setVisibility(View.VISIBLE);
        } else {
            statusView.setVisibility(View.GONE);
        }
        errorView.setVisibility(View.GONE);
    }

    /**
     * Shows error message
     */
    private void showError(String error) {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);
        errorView.setText(error);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (geminiClient != null) {
            geminiClient.shutdown();
        }
    }
}
