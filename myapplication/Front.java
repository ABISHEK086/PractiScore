package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class Front extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        // Play background video
        VideoView videoBackground = findViewById(R.id.video_background);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.practiscore_bg);
        videoBackground.setVideoURI(videoUri);
        videoBackground.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(0, 0); // Mute video
            videoBackground.start();
        });

        // Animate Welcome Text
        TextView welcomeText = findViewById(R.id.welcome_text);
        animateWelcomeText(welcomeText);

        // Button click listener with animation
        Button getStartedButton = findViewById(R.id.bottom_button);
        getStartedButton.setOnClickListener(v -> {
            ScaleAnimation scaleAnim = new ScaleAnimation(
                    1.0f, 0.9f,
                    1.0f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleAnim.setDuration(200);
            scaleAnim.setFillAfter(true);

            getStartedButton.startAnimation(scaleAnim);

            getStartedButton.postDelayed(() -> {
                Intent intent = new Intent(Front.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 200);
        });
    }

    private void animateWelcomeText(TextView textView) {
        textView.setVisibility(View.VISIBLE);

        // Fade-in animation
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500);

        // Scale animation
        ScaleAnimation scaleUp = new ScaleAnimation(
                0.5f, 1.0f,
                0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleUp.setDuration(1500);

        // Combine animations
        textView.startAnimation(fadeIn);
        textView.startAnimation(scaleUp);
    }
}
