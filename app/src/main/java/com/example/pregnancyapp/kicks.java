package com.example.pregnancyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class kicks extends AppCompatActivity {

    private TextView timerText, kickCountText;
    private boolean isTimerRunning = false;
    private long startTime = 0;
    private final Handler timerHandler = new Handler();
    private int minutes = 0, seconds = 0, kickCount = 0;

    // Firebase Realtime Database reference
    private DatabaseReference databaseReference;

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

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("kick_records");

        findViewById(R.id.butnHome).setOnClickListener(v ->
                startActivity(new Intent(kicks.this, home.class)));

        // Initialize UI elements
        kickCountText = findViewById(R.id.kickCount);
        timerText = findViewById(R.id.timerText);
        Button kickButton = findViewById(R.id.kickButton);
        Button resetButton = findViewById(R.id.resetButton);
        Button saveButton = findViewById(R.id.saveButton);

        // Kick Button Click
        kickButton.setOnClickListener(v -> {
            kickCount++;
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

        // Save Button Click — save kick data to Firebase
        saveButton.setOnClickListener(v -> {
            long saveTimestamp = System.currentTimeMillis();

            KickRecord record = new KickRecord(kickCount, minutes, seconds, saveTimestamp);

            String recordId = databaseReference.push().getKey();

            if (recordId != null) {
                databaseReference.child(recordId).setValue(record)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(kicks.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(kicks.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(kicks.this, "Failed to generate record ID", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Firebase model class for kick data
    public static class KickRecord {
        public int kickCount;
        public int minutes;
        public int seconds;
        public long timestamp;

        // Required empty constructor
        public KickRecord() {}

        public KickRecord(int kickCount, int minutes, int seconds, long timestamp) {
            this.kickCount = kickCount;
            this.minutes = minutes;
            this.seconds = seconds;
            this.timestamp = timestamp;
        }
    }
}
