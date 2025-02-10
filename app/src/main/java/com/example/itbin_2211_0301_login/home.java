package com.example.itbin_2211_0301_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.itbin_2211_0301_login.DailyActivitiesActivity;
import com.example.itbin_2211_0301_login.R;
import com.example.itbin_2211_0301_login.VideoActivity;
import com.example.itbin_2211_0301_login.healthinput;
import com.example.itbin_2211_0301_login.kicks;
import com.example.itbin_2211_0301_login.mentalhealth;

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
                startActivity(new Intent(home.this, emergencyinfo.class)));
    }
}
