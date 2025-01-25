package com.example.itbin_2211_0301_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usernameEditText = findViewById(R.id.username_input);
        final EditText passwordEditText = findViewById(R.id.password_input);
        final Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Assuming successful login (replace with actual authentication logic)
                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Start the healthinput activity and pass username
                Intent intent = new Intent(MainActivity.this, healthinput.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }
}