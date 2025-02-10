package com.example.itbin_2211_0301_login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Nutrition Tips
        ImageView nutritionImage = findViewById(R.id.nutritionImage);
        TextView nutritionTitle = findViewById(R.id.nutritionTitle);
        Button nutritionButton = findViewById(R.id.nutritionButton);

        // Mother Skincare
        ImageView skincareImage = findViewById(R.id.skincareImage);
        TextView skincareTitle = findViewById(R.id.skincareTitle);
        Button skincareButton = findViewById(R.id.skincareButton);

        // Yoga
        ImageView yogaImage = findViewById(R.id.yogaImage);
        TextView yogaTitle = findViewById(R.id.yogaTitle);
        Button yogaButton = findViewById(R.id.yogaButton);

        // Set Click Listeners
        setClickListener(nutritionImage, "https://youtu.be/7-LY2Fq3Pmo?si=JRQXKGytL1_JcwYg");
        setClickListener(nutritionTitle, "https://youtu.be/7-LY2Fq3Pmo?si=JRQXKGytL1_JcwYg");
        setClickListener(nutritionButton, "https://youtu.be/7-LY2Fq3Pmo?si=JRQXKGytL1_JcwYg");

        setClickListener(skincareImage, "https://youtu.be/ac5IyS4F6m8?si=pjGKQawouFI5buOo");
        setClickListener(skincareTitle, "https://youtu.be/ac5IyS4F6m8?si=pjGKQawouFI5buOo");
        setClickListener(skincareButton, "https://youtu.be/ac5IyS4F6m8?si=pjGKQawouFI5buOo");

        setClickListener(yogaImage, "https://youtu.be/B87FpWtkIKA?si=khatQBdmoyIovryC");
        setClickListener(yogaTitle, "https://youtu.be/B87FpWtkIKA?si=khatQBdmoyIovryC");
        setClickListener(yogaButton, "https://youtu.be/B87FpWtkIKA?si=khatQBdmoyIovryC");
    }

    private void setClickListener(View view, String url) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}
