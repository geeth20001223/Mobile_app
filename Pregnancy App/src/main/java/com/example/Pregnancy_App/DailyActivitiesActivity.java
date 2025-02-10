package com.example.Pregnancy_App;  // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Pregnancy_App.R;



public class DailyActivitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_activities);

        findViewById(R.id.btnhome).setOnClickListener(v ->
                startActivity(new Intent(DailyActivitiesActivity.this, home.class)));

        // Declare these as local variables
        CheckBox checkWater = findViewById(R.id.checkWater);
        CheckBox checkYoga = findViewById(R.id.checkYoga);
        CheckBox checkHealthyFood = findViewById(R.id.checkHealthyFood);
        CheckBox checkMusic = findViewById(R.id.checkMusic);
        CheckBox checkMeditation = findViewById(R.id.checkMeditation);
        Button btnCheckActivities = findViewById(R.id.btnCheckActivities);

        // Button click event
        btnCheckActivities.setOnClickListener(v -> checkDailyActivities(checkWater, checkYoga, checkHealthyFood, checkMusic, checkMeditation));
    }

    private void checkDailyActivities(CheckBox checkWater, CheckBox checkYoga, CheckBox checkHealthyFood, CheckBox checkMusic, CheckBox checkMeditation) {
        if (checkWater.isChecked() || checkYoga.isChecked() || checkHealthyFood.isChecked()
                || checkMusic.isChecked() || checkMeditation.isChecked()) {
            // Using getString() to fetch string resource for the "Well done" message
            Toast.makeText(this, getString(R.string.well_done), Toast.LENGTH_SHORT).show();
        } else {
            // Using getString() to fetch string resource for the "Do better tomorrow" message
            Toast.makeText(this, getString(R.string.do_better), Toast.LENGTH_SHORT).show();
        }
    }
}
