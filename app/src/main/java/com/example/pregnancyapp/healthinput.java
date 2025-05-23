package com.example.pregnancyapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.FirebaseDatabase;

public class healthinput extends AppCompatActivity {

    private static final String TAG = "HealthInputActivity";
    private static final String CHANNEL_ID = "health_data_channel";

    EditText weightEditText, systolicEditText, diastolicEditText, bodyTemperatureEditText, symptomsEditText;
    Button saveButton, homeButton;  // Added homeButton here
    CalendarView calendarView;
    String selectedDate;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notification permission granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notification permission denied. Cannot show notifications.", Toast.LENGTH_LONG).show();
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                        showSettingsDialog();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_input);

        weightEditText = findViewById(R.id.weightEditText);
        systolicEditText = findViewById(R.id.systolicEditText);
        diastolicEditText = findViewById(R.id.diastolicEditText);
        bodyTemperatureEditText = findViewById(R.id.bodyTemperatureEditText);
        symptomsEditText = findViewById(R.id.symptomsEditText);
        saveButton = findViewById(R.id.saveButton);
        calendarView = findViewById(R.id.calendarView);
        homeButton = findViewById(R.id.btnHome); // Initialize home button

        selectedDate = getFormattedDate(calendarView.getDate());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
        });

        saveButton.setOnClickListener(v -> {
            String weight = weightEditText.getText().toString();
            String sys = systolicEditText.getText().toString();
            String dia = diastolicEditText.getText().toString();
            String temp = bodyTemperatureEditText.getText().toString();
            String symptoms = symptomsEditText.getText().toString();

            if (weight.isEmpty() || sys.isEmpty() || dia.isEmpty() || temp.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields (Weight, BP, Temperature)", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseDatabase.getInstance().getReference("HealthData")
                    .child(selectedDate)
                    .setValue(new HealthData(weight, sys, dia, temp, symptoms))
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Data saved successfully to Firebase.");
                        Toast.makeText(this, "Health Data Saved Successfully!", Toast.LENGTH_SHORT).show();
                        showNotification("Health Data Saved", "Your health data for " + selectedDate + " was successfully saved.");
                        clearFields();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error saving data to Firebase: " + e.getMessage(), e);
                        Toast.makeText(this, "Error saving data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        // Home button click listener to navigate back to home
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(healthinput.this, home.class); // <-- replace HomeActivity.class with your home activity class
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        createNotificationChannel();
        requestNotificationPermission();
    }

    void clearFields() {
        weightEditText.setText("");
        systolicEditText.setText("");
        diastolicEditText.setText("");
        bodyTemperatureEditText.setText("");
        symptomsEditText.setText("");
    }

    String getFormattedDate(long millis) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(millis);
    }

    void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Health Data Notifications";
            String description = "Notifications for health data saving status.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel created.");
            } else {
                Log.e(TAG, "NotificationManager is null. Cannot create channel.");
            }
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                    Toast.makeText(this, "Notification permission is needed to show data saving updates.", Toast.LENGTH_LONG).show();
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            } else {
                Log.d(TAG, "Notification permission already granted.");
            }
        }
    }

    void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (channel == null || channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Log.w(TAG, "Notifications are disabled for this channel or app-wide. Cannot show notification.");
                Toast.makeText(this, "Notifications are disabled. Check app settings.", Toast.LENGTH_LONG).show();
                showSettingsDialog();
                return;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "POST_NOTIFICATIONS permission not granted. Cannot show notification.");
                Toast.makeText(this, "Permission to post notifications is denied.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        try {
            NotificationManagerCompat.from(this).notify(1, builder.build());
            Log.d(TAG, "Notification shown successfully.");
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException: No permission to post notifications. " + e.getMessage());
            Toast.makeText(this, "Failed to show notification. Check app permissions.", Toast.LENGTH_LONG).show();
        }
    }

    private void showSettingsDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Notification Permission Required")
                .setMessage("To receive data saving notifications, please enable notifications in app settings.")
                .setPositiveButton("Open Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    static class HealthData {
        public String weight;
        public String systolic;
        public String diastolic;
        public String temperature;
        public String symptoms;

        public HealthData() {}

        public HealthData(String w, String s, String d, String t, String sym) {
            this.weight = w;
            this.systolic = s;
            this.diastolic = d;
            this.temperature = t;
            this.symptoms = sym;
        }
    }
}
