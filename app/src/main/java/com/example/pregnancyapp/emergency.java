package com.example.pregnancyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class emergency extends AppCompatActivity {

    private TextView tvHospitals, tvDoctors;
    private ListView listHospitals, listDoctors;
    private DatabaseReference dbRef;

    private ArrayList<String> hospitalList;
    private ArrayAdapter<String> hospitalAdapter;

    private ArrayList<String> doctorList;
    private ArrayAdapter<String> doctorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_info);

        // Button to go to Home
        Button btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v ->
                startActivity(new Intent(emergency.this, home.class)));

        // Initialize views
        tvHospitals = findViewById(R.id.tvHospitals);
        tvDoctors = findViewById(R.id.tvDoctors);
        listHospitals = findViewById(R.id.listHospitals);
        listDoctors = findViewById(R.id.listDoctors);
        Button btnShowHospitals = findViewById(R.id.btnShowHospitals);

        // Initialize Firebase reference
        dbRef = FirebaseDatabase.getInstance().getReference("emergencyInfo");

        // Setup adapters
        hospitalList = new ArrayList<>();
        doctorList = new ArrayList<>();
        hospitalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hospitalList);
        doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctorList);

        listHospitals.setAdapter(hospitalAdapter);
        listDoctors.setAdapter(doctorAdapter);

        // Show hospitals button
        btnShowHospitals.setOnClickListener(v -> loadHospitals());
    }

    private void loadHospitals() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hospitalList.clear();
                for (DataSnapshot hospitalSnapshot : snapshot.getChildren()) {
                    hospitalList.add(hospitalSnapshot.getKey());
                }

                if (!hospitalList.isEmpty()) {
                    tvHospitals.setVisibility(View.VISIBLE);
                    listHospitals.setVisibility(View.VISIBLE);
                    hospitalAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(emergency.this, "No hospitals found.", Toast.LENGTH_SHORT).show();
                }

                // Handle click on hospital to load doctors
                listHospitals.setOnItemClickListener((parent, view, position, id) -> {
                    String selectedHospital = hospitalList.get(position);
                    loadDoctors(selectedHospital);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(emergency.this, "Failed to load hospitals.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDoctors(String hospitalName) {
        dbRef.child(hospitalName).child("doctors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        doctorList.clear();
                        for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                            doctorList.add(doctorSnapshot.getValue(String.class));
                        }

                        if (!doctorList.isEmpty()) {
                            tvDoctors.setVisibility(View.VISIBLE);
                            listDoctors.setVisibility(View.VISIBLE);
                            doctorAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(emergency.this, "No doctors found for this hospital.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(emergency.this, "Failed to load doctors.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
