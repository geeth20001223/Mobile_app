package com.example.pregnancyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class home extends AppCompatActivity {

    private TextView weekNumberText;

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

    private void loadPregnancyWeek() {
        String userId = "userId123"; // Replace this with FirebaseAuth.getInstance().getCurrentUser().getUid(); if using authentication
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userData").child(userId);

        ref.child("pregnancyStartDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String startDateStr = snapshot.getValue(String.class);
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date startDate = sdf.parse(startDateStr);
                        Date today = new Date();

                        long diffInMillis = today.getTime() - startDate.getTime();
                        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                        int currentWeek = (int) (diffInDays / 7) + 1;

                        weekNumberText.setText("Week " + currentWeek);
                    } catch (Exception e) {
                        weekNumberText.setText("Week: N/A");
                        Toast.makeText(home.this, "Date parsing error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    weekNumberText.setText("Week: Not Set");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(home.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
