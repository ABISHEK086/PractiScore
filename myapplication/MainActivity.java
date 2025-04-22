package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private VideoView videoBackground;
    private TextView marqueeText;
    private CardView studentCard, staffCard, adminCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Video Background
        videoBackground = findViewById(R.id.videoBackground);
        playBackgroundVideo();  // Start video

        // Initialize Views
        marqueeText = findViewById(R.id.marqueeText);
        studentCard = findViewById(R.id.d1);
        staffCard = findViewById(R.id.d2);
        adminCard = findViewById(R.id.d3);

        // Load Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        // Apply Animations
        marqueeText.startAnimation(fadeIn);
        studentCard.startAnimation(slideIn);
        staffCard.startAnimation(slideIn);
        adminCard.startAnimation(slideIn);

        // Set onClickListeners for Navigation
        studentCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginStudent.class);
            overridePendingTransition(R.anim.fade_in, R.anim.slide_in_bottom); // Smooth transition
            startActivity(intent);
        });

        staffCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginStaff.class);
            overridePendingTransition(R.anim.fade_in, R.anim.slide_in_bottom);
            startActivity(intent);
        });

        adminCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginAdmin.class);
            overridePendingTransition(R.anim.fade_in, R.anim.slide_in_bottom);
            startActivity(intent);
        });
    }

    // Method to Play Video Background
    private void playBackgroundVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_video);
        videoBackground.setVideoURI(videoUri);
        videoBackground.start();

        // Ensure the video loops continuously
        videoBackground.setOnCompletionListener(mp -> videoBackground.start());
    }

    @Override
    protected void onResume() {
        super.onResume();
        playBackgroundVideo(); // Restart video when returning to this activity
    }
}
