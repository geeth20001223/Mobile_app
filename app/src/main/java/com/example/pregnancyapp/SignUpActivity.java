package com.example.pregnancyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private Button signInButton;

    private FirebaseAuth mAuth; // Firebase Auth instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.imauser);

        // Sign Up button logic
        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String fullName = fullNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase signup with email and password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up success
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();

                                // Optionally, you can update user profile with username/fullName here

                                // Navigate to Login screen or directly to main app screen
                                Intent intent = new Intent(SignUpActivity.this, login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Sign up failed
                                Toast.makeText(SignUpActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });

        // "I'm already a user" button logic
        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, login.class);
            startActivity(intent);
            finish();
        });
    }
}
