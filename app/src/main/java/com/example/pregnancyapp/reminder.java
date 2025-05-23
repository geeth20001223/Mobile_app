package com.example.pregnancyapp;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class reminder extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText reminderInput;
    private Button addButton, editButton, deleteButton, saveButton, homeButton;
    private ListView reminderList;
    private TextView reminderDisplayTitle;

    private String selectedDate = "";
    private ArrayList<String> reminders;
    private ArrayAdapter<String> adapter;

    // Firebase reference
    private DatabaseReference remindersDatabase;

    // Map to keep track of Firebase keys for each reminder string
    private HashMap<String, String> reminderKeyMap;

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

        reminders = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, reminders);
        reminderList.setAdapter(adapter);
        reminderList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        reminderDisplayTitle.setVisibility(View.GONE);
        reminderList.setVisibility(View.GONE);

        // Initialize Firebase reference
        remindersDatabase = FirebaseDatabase.getInstance().getReference("reminders");

        // Initialize key map
        reminderKeyMap = new HashMap<>();

        // Load reminders from Firebase
        loadRemindersFromFirebase();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(reminder.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
        });

        addButton.setOnClickListener(v -> {
            String reminderText = reminderInput.getText().toString().trim();
            if (selectedDate.isEmpty()) {
                Toast.makeText(reminder.this, "Please select a date first!", Toast.LENGTH_SHORT).show();
            } else if (reminderText.isEmpty()) {
                Toast.makeText(reminder.this, "Enter a reminder!", Toast.LENGTH_SHORT).show();
            } else {
                String reminderFullText = selectedDate + ": " + reminderText;
                saveReminderToFirebase(reminderFullText);
                reminderInput.setText("");
            }
        });

        editButton.setOnClickListener(v -> {
            int position = reminderList.getCheckedItemPosition();
            if (reminders.isEmpty()) {
                Toast.makeText(reminder.this, "No reminders to edit!", Toast.LENGTH_SHORT).show();
            } else if (position != -1) {
                String newText = reminderInput.getText().toString().trim();
                if (!newText.isEmpty()) {
                    String updatedReminder = selectedDate + ": " + newText;
                    String oldReminder = reminders.get(position);
                    updateReminderInFirebase(oldReminder, updatedReminder);
                    reminderInput.setText("");
                } else {
                    Toast.makeText(reminder.this, "Enter a new reminder text!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(reminder.this, "Select a reminder to edit!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            int position = reminderList.getCheckedItemPosition();
            if (position != -1) {
                String reminderToDelete = reminders.get(position);
                deleteReminderFromFirebase(reminderToDelete);
            } else {
                Toast.makeText(reminder.this, "Select a reminder to delete!", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(v -> {
            if (reminders.isEmpty()) {
                Toast.makeText(reminder.this, "No reminders to save!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(reminder.this, "Reminders saved!", Toast.LENGTH_SHORT).show();
            }
        });

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(reminder.this, home.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadRemindersFromFirebase() {
        remindersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reminders.clear();
                reminderKeyMap.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String key = child.getKey();
                    String reminderText = child.getValue(String.class);
                    if (reminderText != null) {
                        reminders.add(reminderText);
                        reminderKeyMap.put(reminderText, key);
                    }
                }
                adapter.notifyDataSetChanged();
                updateReminderVisibility();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(reminder.this, "Failed to load reminders.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveReminderToFirebase(String reminder) {
        String key = remindersDatabase.push().getKey();
        if (key != null) {
            remindersDatabase.child(key).setValue(reminder)
                    .addOnSuccessListener(aVoid -> Toast.makeText(reminder.this, "Reminder added!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(reminder.this, "Failed to add reminder.", Toast.LENGTH_SHORT).show());
        }
    }

    private void updateReminderInFirebase(String oldReminder, String updatedReminder) {
        String key = reminderKeyMap.get(oldReminder);
        if (key != null) {
            remindersDatabase.child(key).setValue(updatedReminder)
                    .addOnSuccessListener(aVoid -> Toast.makeText(reminder.this, "Reminder updated!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(reminder.this, "Failed to update reminder.", Toast.LENGTH_SHORT).show());
        }
    }

    private void deleteReminderFromFirebase(String reminder) {
        String key = reminderKeyMap.get(reminder);
        if (key != null) {
            remindersDatabase.child(key).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(reminder.this, "Reminder deleted!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(reminder.this, "Failed to delete reminder.", Toast.LENGTH_SHORT).show());
        }
    }

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
