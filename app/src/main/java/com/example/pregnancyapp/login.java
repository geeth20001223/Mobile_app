package com.example.pregnancyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        final EditText emailEditText = findViewById(R.id.username_input); // Assuming this is email
        final EditText passwordEditText = findViewById(R.id.password_input);
        final Button loginButton = findViewById(R.id.login_button);
        final Button backButton = findViewById(R.id.backButton);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Login successful
                                Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(login.this, blossom.class);
                                intent.putExtra("username", email); // You can fetch full name from Firestore if needed
                                startActivity(intent);
                                finish();
                            } else {
                                // Login failed
                                Toast.makeText(login.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
