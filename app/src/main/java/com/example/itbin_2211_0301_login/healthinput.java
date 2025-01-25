package com.example.itbin_2211_0301_login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class healthinput extends Activity {

    private EditText weightEditText, systolicEditText, diastolicEditText, bodyTemperatureEditText, symptomsEditText;
    private Button saveButton;
    private CalendarView calendarView;
    private final Map<String, HealthData> healthDataMap = new HashMap<>();

    private static class HealthData {
        String weight;
        String systolic;
        String diastolic;
        String bodyTemperature;
        String symptoms;
    }

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

        // Handle calendar view selection
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                HealthData data = healthDataMap.get(date);
                if (data != null) {
                    weightEditText.setText(data.weight);
                    systolicEditText.setText(data.systolic);
                    diastolicEditText.setText(data.diastolic);
                    bodyTemperatureEditText.setText(data.bodyTemperature);
                    symptomsEditText.setText(data.symptoms);
                } else {
                    clearInputFields();
                }
            }
        });

        saveButton.setOnClickListener(v -> {
            String weight = weightEditText.getText().toString();
            String systolic = systolicEditText.getText().toString();
            String diastolic = diastolicEditText.getText().toString();
            String bodyTemp = bodyTemperatureEditText.getText().toString();
            String symptoms = symptomsEditText.getText().toString();

            if (weight.isEmpty() || systolic.isEmpty() || diastolic.isEmpty() || bodyTemp.isEmpty()) {
                Toast.makeText(healthinput.this, "Please enter all values.", Toast.LENGTH_SHORT).show();
                return;
            }

            String date = String.format("%d-%02d-%02d", calendarView.getY(), calendarView.getWidth() + 1, calendarView.getDate());

            HealthData data = new HealthData();
            data.weight = weight;
            data.systolic = systolic;
            data.diastolic = diastolic;
            data.bodyTemperature = bodyTemp;
            data.symptoms = symptoms;

            healthDataMap.put(date, data);

            // Display success message and clear fields
            Toast.makeText(healthinput.this, "Data Saved", Toast.LENGTH_SHORT).show();
            clearInputFields();
        });
    }

    private void clearInputFields() {
        weightEditText.setText("");
        systolicEditText.setText("");
        diastolicEditText.setText("");
        bodyTemperatureEditText.setText("");
        symptomsEditText.setText("");
    }
}