package com.example.Pregnancy_App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Pregnancy_App.R;


import java.util.ArrayList;

public class reminder extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText reminderInput;
    private Button addButton, editButton, deleteButton, saveButton, homeButton;
    private ListView reminderList;
    private TextView reminderDisplayTitle;

    private String selectedDate = "";
    private ArrayList<String> reminders;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Initialize Views
        calendarView = findViewById(R.id.calendarView);
        reminderInput = findViewById(R.id.reminderInput);
        addButton = findViewById(R.id.addButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);
        homeButton = findViewById(R.id.homeButton);
        reminderList = findViewById(R.id.reminderList);
        reminderDisplayTitle = findViewById(R.id.reminderDisplayTitle);

        // Initialize reminder storage
        reminders = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, reminders);
        reminderList.setAdapter(adapter);
        reminderList.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // Allow single item selection

        // Hide reminder display initially
        reminderDisplayTitle.setVisibility(View.GONE);
        reminderList.setVisibility(View.GONE);

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
            String reminderText = reminderInput.getText().toString().trim();
            if (selectedDate.isEmpty()) {
                Toast.makeText(reminder.this, "Please select a date first!", Toast.LENGTH_SHORT).show();
            } else if (reminderText.isEmpty()) {
                Toast.makeText(reminder.this, "Enter a reminder!", Toast.LENGTH_SHORT).show();
            } else {
                reminders.add(selectedDate + ": " + reminderText);
                adapter.notifyDataSetChanged();
                reminderInput.setText("");
                updateReminderVisibility();
            }
        });

        // Edit button listener
        editButton.setOnClickListener(v -> {
            if (reminderList.getCount() == 0) {
                Toast.makeText(reminder.this, "No reminders to edit!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (reminderList.getCheckedItemPosition() != -1) {
                String updatedReminder = reminderInput.getText().toString().trim();
                if (!updatedReminder.isEmpty()) {
                    reminders.set(reminderList.getCheckedItemPosition(), selectedDate + ": " + updatedReminder);
                    adapter.notifyDataSetChanged();
                    reminderInput.setText("");
                } else {
                    Toast.makeText(reminder.this, "Enter a new reminder text!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(reminder.this, "Select a reminder to edit!", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete button listener
        deleteButton.setOnClickListener(v -> {
            int position = reminderList.getCheckedItemPosition(); // Get the selected position
            if (position != -1) {
                reminders.remove(position); // Remove the selected reminder
                adapter.notifyDataSetChanged(); // Update the adapter
                updateReminderVisibility(); // Update the visibility of reminder section
                reminderList.clearChoices(); // Clear the selected item
            } else {
                Toast.makeText(reminder.this, "Select a reminder to delete!", Toast.LENGTH_SHORT).show();
            }
        });

        // Save button listener
        saveButton.setOnClickListener(v -> {
            if (reminders.isEmpty()) {
                Toast.makeText(reminder.this, "No reminders to save!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(reminder.this, "Reminders saved!", Toast.LENGTH_SHORT).show();
            }
        });

        // Home button listener (Navigate to Main Activity)
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(reminder.this, home.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to show or hide reminder section based on list count
    private void updateReminderVisibility() {
        if (reminders.isEmpty()) {
            reminderDisplayTitle.setVisibility(View.GONE);
            reminderList.setVisibility(View.GONE);
        } else {
            reminderDisplayTitle.setVisibility(View.VISIBLE);
            reminderList.setVisibility(View.VISIBLE);
        }
    }
}
