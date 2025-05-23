package com.example.pregnancyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class mentalhealth extends AppCompatActivity {

    private EditText inputMentalNote;
    private Button saveNoteButton, btnHome;
    private TextView displayNotes;

    private DatabaseReference notesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentalhealth);

        // Initialize views
        inputMentalNote = findViewById(R.id.inputMentalNote);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        displayNotes = findViewById(R.id.displayNotes);
        btnHome = findViewById(R.id.btnHome);

        // Initialize Firebase database reference under "mental_notes"
        notesDatabase = FirebaseDatabase.getInstance().getReference("mental_notes");

        // Save button click: Save input text to Firebase
        saveNoteButton.setOnClickListener(v -> {
            String note = inputMentalNote.getText().toString().trim();
            if (note.isEmpty()) {
                Toast.makeText(mentalhealth.this, "Please write something before saving", Toast.LENGTH_SHORT).show();
                return;
            }
            saveNoteToFirebase(note);
        });

        // Home button click: Go back to home activity
        btnHome.setOnClickListener(v ->
                startActivity(new Intent(mentalhealth.this, home.class)));

        // Listen for data changes and update displayNotes TextView
        notesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                StringBuilder allNotes = new StringBuilder();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    String noteText = noteSnapshot.child("text").getValue(String.class);
                    allNotes.append("- ").append(noteText).append("\n\n");
                }
                displayNotes.setText(allNotes.toString().trim());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(mentalhealth.this, "Failed to load notes.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNoteToFirebase(String note) {
        // Generate a unique key for each note
        String id = notesDatabase.push().getKey();

        if (id != null) {
            // Create a simple Note object
            Note noteObj = new Note(note);

            // Save the note under the generated ID
            notesDatabase.child(id).setValue(noteObj)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(mentalhealth.this, "Note saved!", Toast.LENGTH_SHORT).show();
                        inputMentalNote.setText(""); // Clear input field after saving
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(mentalhealth.this, "Failed to save note.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Simple Note class to hold text
    public static class Note {
        public String text;

        // Empty constructor needed for Firebase
        public Note() {
        }

        public Note(String text) {
            this.text = text;
        }
    }
}
