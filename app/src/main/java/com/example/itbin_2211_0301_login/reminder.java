package com.example.itbin_2211_0301_login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class reminder extends AppCompatActivity {

    private CalendarView calendarView;
    private Button addButton, editButton, deleteButton;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Initialize Views
        calendarView = findViewById(R.id.calendarView);
        addButton = findViewById(R.id.addButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Set initial selected date
        selectedDate = "";

        // CalendarView listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(reminder.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });

        // Add button listener
        addButton.setOnClickListener(v -> {
            if (selectedDate.isEmpty()) {
                Toast.makeText(reminder.this, "Please select a date first!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(reminder.this, "Add reminder for " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });

        // Edit button listener
        editButton.setOnClickListener(v -> {
            if (selectedDate.isEmpty()) {
                Toast.makeText(reminder.this, "Please select a date first!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(reminder.this, "Edit reminder for " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });

        // Delete button listener
        deleteButton.setOnClickListener(v -> {
            if (selectedDate.isEmpty()) {
                Toast.makeText(reminder.this, "Please select a date first!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(reminder.this, "Delete reminder for " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });
    }
}