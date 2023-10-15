package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivities extends AppCompatActivity {
    private static int SPLASH_SCREEN = 3000;
    // Variables
    private Animation topAni, bottomAni;
    private ImageView image;
    private TextView team, slogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activities_main);

        // Animations
        topAni = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAni = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        //Hooks
        image = findViewById(R.id.imageView);
        team = findViewById(R.id.teamName);
        slogan = findViewById(R.id.slogan);

        image.setAnimation(topAni);
        team.setAnimation(bottomAni);
        slogan.setAnimation(bottomAni);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivities.this, MainActivity.class));
                finish();
            }
        },SPLASH_SCREEN);
    }
}