package com.example.pregnancyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VideoActivity extends AppCompatActivity {

    private DatabaseReference videoLinksRef;

    // UI elements
    private ImageView nutritionImage, skincareImage, yogaImage;
    private TextView nutritionTitle, skincareTitle, yogaTitle;
    private Button nutritionButton, skincareButton, yogaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        findViewById(R.id.homeButton).setOnClickListener(v ->
                startActivity(new Intent(VideoActivity.this, home.class)));

        // Initialize UI elements
        nutritionImage = findViewById(R.id.nutritionImage);
        nutritionTitle = findViewById(R.id.nutritionTitle);
        nutritionButton = findViewById(R.id.nutritionButton);

        skincareImage = findViewById(R.id.skincareImage);
        skincareTitle = findViewById(R.id.skincareTitle);
        skincareButton = findViewById(R.id.skincareButton);

        yogaImage = findViewById(R.id.yogaImage);
        yogaTitle = findViewById(R.id.yogaTitle);
        yogaButton = findViewById(R.id.yogaButton);

        // Firebase reference to video links
        videoLinksRef = FirebaseDatabase.getInstance().getReference("video_links");

        // Load URLs and set listeners dynamically
        videoLinksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nutritionUrl = snapshot.child("nutrition").getValue(String.class);
                String skincareUrl = snapshot.child("skincare").getValue(String.class);
                String yogaUrl = snapshot.child("yoga").getValue(String.class);

                if (nutritionUrl != null) {
                    setClickListener(nutritionImage, nutritionUrl);
                    setClickListener(nutritionTitle, nutritionUrl);
                    setClickListener(nutritionButton, nutritionUrl);
                }

                if (skincareUrl != null) {
                    setClickListener(skincareImage, skincareUrl);
                    setClickListener(skincareTitle, skincareUrl);
                    setClickListener(skincareButton, skincareUrl);
                }

                if (yogaUrl != null) {
                    setClickListener(yogaImage, yogaUrl);
                    setClickListener(yogaTitle, yogaUrl);
                    setClickListener(yogaButton, yogaUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VideoActivity.this, "Failed to load video links.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setClickListener(View view, String url) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}
