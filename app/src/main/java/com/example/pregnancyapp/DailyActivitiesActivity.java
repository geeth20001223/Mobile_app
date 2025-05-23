package com.example.pregnancyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailyActivitiesActivity extends AppCompatActivity {

    private CheckBox checkWater, checkYoga, checkHealthyFood, checkMusic, checkMeditation;
    private Button btnCheckActivities;

    private DatabaseReference activitiesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_activities);

        findViewById(R.id.btnhome).setOnClickListener(v ->
                startActivity(new Intent(DailyActivitiesActivity.this, home.class)));

        // Initialize Firebase reference
        activitiesDatabase = FirebaseDatabase.getInstance().getReference("daily_activities");

        // Initialize checkboxes and button
        checkWater = findViewById(R.id.checkWater);
        checkYoga = findViewById(R.id.checkYoga);
        checkHealthyFood = findViewById(R.id.checkHealthyFood);
        checkMusic = findViewById(R.id.checkMusic);
        checkMeditation = findViewById(R.id.checkMeditation);
        btnCheckActivities = findViewById(R.id.btnCheckActivities);

        btnCheckActivities.setOnClickListener(v -> {
            // Save checkbox states to Firebase
            saveDailyActivitiesToFirebase();
            // Show toast message based on activity
            checkDailyActivities();
        });
    }

    private void saveDailyActivitiesToFirebase() {
        String id = activitiesDatabase.push().getKey();
        if (id == null) return;

        DailyActivities activities = new DailyActivities(
                checkWater.isChecked(),
                checkYoga.isChecked(),
                checkHealthyFood.isChecked(),
                checkMusic.isChecked(),
                checkMeditation.isChecked()
        );

        activitiesDatabase.child(id).setValue(activities)
                .addOnSuccessListener(aVoid -> Toast.makeText(DailyActivitiesActivity.this, "Activities saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(DailyActivitiesActivity.this, "Failed to save activities.", Toast.LENGTH_SHORT).show());
    }

    private void checkDailyActivities() {
        if (checkWater.isChecked() || checkYoga.isChecked() || checkHealthyFood.isChecked()
                || checkMusic.isChecked() || checkMeditation.isChecked()) {
            Toast.makeText(this, getString(R.string.well_done), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.do_better), Toast.LENGTH_SHORT).show();
        }
    }

    // Model class for Daily Activities
    public static class DailyActivities {
        public boolean water;
        public boolean yoga;
        public boolean healthyFood;
        public boolean music;
        public boolean meditation;

        // Default constructor required for calls to DataSnapshot.getValue(DailyActivities.class)
        public DailyActivities() {
        }

        public DailyActivities(boolean water, boolean yoga, boolean healthyFood, boolean music, boolean meditation) {
            this.water = water;
            this.yoga = yoga;
            this.healthyFood = healthyFood;
            this.music = music;
            this.meditation = meditation;
        }
    }
}
