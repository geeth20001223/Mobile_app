package com.example.itbin_2211_0301_login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class kicks extends AppCompatActivity {

    private TextView timerText, kickCountText;
    private boolean isTimerRunning = false;
    private long startTime = 0;
    private final Handler timerHandler = new Handler();
    private int minutes = 0, seconds = 0, kickCount = 0; // ✅ Class-level variables

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedMillis = System.currentTimeMillis() - startTime;
            seconds = (int) (elapsedMillis / 1000) % 60;
            minutes = (int) (elapsedMillis / 1000) / 60;

            runOnUiThread(() -> timerText.setText(String.format(Locale.getDefault(), "Elapsed Time: %02d:%02d", minutes, seconds)));
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kicks);

        // Initialize UI elements
        kickCountText = findViewById(R.id.kickCount);
        timerText = findViewById(R.id.timerText);
        Button kickButton = findViewById(R.id.kickButton);
        Button resetButton = findViewById(R.id.resetButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button callDoctorButton = findViewById(R.id.callDoctorButton);

        // Kick Button Click
        kickButton.setOnClickListener(v -> {
            kickCount++; // ✅ Now increments correctly
            kickCountText.setText(getString(R.string.kicks_text, kickCount));

            if (!isTimerRunning) {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                isTimerRunning = true;
            }
        });

        // Reset Button Click
        resetButton.setOnClickListener(v -> {
            kickCount = 0;
            kickCountText.setText(getString(R.string.kicks_text, kickCount));
            isTimerRunning = false;
            timerHandler.removeCallbacks(timerRunnable);
            minutes = 0;
            seconds = 0;
            timerText.setText(getString(R.string.elapsed_time, minutes, seconds));
        });

        // Save Button Click (For now, just a placeholder)
        saveButton.setOnClickListener(v -> {
            // Future: Save data to SQLite or Firebase
        });

        // Call Doctor Button Click
        callDoctorButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:123456789")); // Change to actual doctor's number
            startActivity(intent);
        });
    }
}
