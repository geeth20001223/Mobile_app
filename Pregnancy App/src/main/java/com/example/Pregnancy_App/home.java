package com.example.Pregnancy_App;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.example.Pregnancy_App.R;


public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set Click Listeners for each image
        findViewById(R.id.trackHealthButton).setOnClickListener(v ->
                startActivity(new Intent(home.this, healthinput.class)));

        findViewById(R.id.kickTrackerButton).setOnClickListener(v ->
                startActivity(new Intent(home.this, kicks.class)));

        findViewById(R.id.educationButton).setOnClickListener(v ->
                startActivity(new Intent(home.this, VideoActivity.class)));

        findViewById(R.id.mentalHealthButton).setOnClickListener(v ->
                startActivity(new Intent(home.this, mentalhealth.class)));

        findViewById(R.id.dailyActivityButton).setOnClickListener(v ->
                startActivity(new Intent(home.this, DailyActivitiesActivity.class)));

        findViewById(R.id.remindersButton).setOnClickListener(v ->
                startActivity(new Intent(home.this, reminder.class)));

        findViewById(R.id.emergencyButton).setOnClickListener(v ->
                startActivity(new Intent(home.this, emergency.class)));
    }
}