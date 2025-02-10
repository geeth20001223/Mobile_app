package com.example.Pregnancy_App;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Pregnancy_App.R;


public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username_input);
        final EditText passwordEditText = findViewById(R.id.password_input);
        final Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();

                // Navigate to SignUpActivity
                Intent intent = new Intent(login.this, home.class);
                startActivity(intent);
                return;
            } else {
                // Assuming successful login (replace with actual authentication logic)
                Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Start the healthinput activity and pass username
                Intent intent = new Intent(login.this, blossom.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }
}
