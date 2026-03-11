package com.example.mobiletechapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // UI and Events button
        Button button = findViewById(R.id.buttonUIEvent);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("message", "Hello World!");
                startActivity(intent);

            }
        });

        // Location Services button
        Button locationButton = findViewById(R.id.buttonLocationServices);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StartActivity.this, LocationServicesActivity.class);
                startActivity(intent);

            }
        });

    }
}