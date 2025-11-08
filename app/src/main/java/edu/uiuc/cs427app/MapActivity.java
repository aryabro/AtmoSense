package edu.uiuc.cs427app;

import android.os.Bundle;
import android.widget.TextView;

public class MapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        String cityName = getIntent().getStringExtra("city");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Map - " + cityName);
        }

        TextView title = findViewById(R.id.mapTitle);
        title.setText("Map for " + cityName);
    }
}
