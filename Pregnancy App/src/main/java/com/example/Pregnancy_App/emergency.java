package com.example.Pregnancy_App;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Pregnancy_App.R;


import java.util.HashMap;

public class emergency extends AppCompatActivity {
    private TextView tvHospitals, tvDoctors;
    private ListView listHospitals, listDoctors;
    private HashMap<String, String[]> doctorsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_info);

        findViewById(R.id.btnHome).setOnClickListener(v ->
                startActivity(new Intent(emergency.this, home.class)));

        tvHospitals = findViewById(R.id.tvHospitals);
        tvDoctors = findViewById(R.id.tvDoctors);
        listHospitals = findViewById(R.id.listHospitals);
        listDoctors = findViewById(R.id.listDoctors);
        Button btnShowHospitals = findViewById(R.id.btnShowHospitals);

        // Define hospital-doctor mapping
        doctorsMap = new HashMap<>();
        doctorsMap.put("Ninewells Hospital", new String[]{"Dr. A - Gynecologist", "Dr. B - Pediatrician"});
        doctorsMap.put("Nawaloka Hospital", new String[]{"Dr. C - Surgeon", "Dr. D - Cardiologist"});
        doctorsMap.put("Lanka Hospital", new String[]{"Dr. E - Orthopedic", "Dr. F - Neurologist"});
        doctorsMap.put("Hemas Hospital", new String[]{"Dr. G - Dentist", "Dr. H - General Physician"});

        btnShowHospitals.setOnClickListener(v -> showHospitals());
    }

    private void showHospitals() {
        // Static list of hospitals
        String[] hospitals = {"Ninewells Hospital", "Nawaloka Hospital", "Lanka Hospital", "Hemas Hospital"};

        // Show hospital section
        tvHospitals.setVisibility(View.VISIBLE);
        listHospitals.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hospitals));

        // Click hospital to show doctors
        listHospitals.setOnItemClickListener((parent, view, position, id) -> {
            String selectedHospital = hospitals[position];
            showDoctors(selectedHospital);
        });
    }

    private void showDoctors(String hospital) {
        // Get doctors of selected hospital
        String[] doctors = doctorsMap.get(hospital);

        // Show doctor section
        tvDoctors.setVisibility(View.VISIBLE);
        listDoctors.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctors));
    }
}
