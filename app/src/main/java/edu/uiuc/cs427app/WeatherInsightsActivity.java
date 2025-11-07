package edu.uiuc.cs427app;

import android.os.Bundle;
import android.widget.TextView;

public class WeatherInsightsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_insights);

        String cityName = getIntent().getStringExtra("city");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Insights - " + cityName);
        }

        TextView title = findViewById(R.id.insightsTitle);
        TextView content = findViewById(R.id.insightsContent);
        title.setText("Weather insights for " + cityName);
        content.setText("Coming soon");
    }
}
