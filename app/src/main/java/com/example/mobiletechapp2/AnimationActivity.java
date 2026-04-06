package com.example.mobiletechapp2;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
    }

    public void start(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageViewAnimation);
        Animation animation = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.basics);
        image.startAnimation(animation);
    }
}