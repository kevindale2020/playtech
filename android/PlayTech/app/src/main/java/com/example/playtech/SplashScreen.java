package com.example.playtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.image);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        imageView.startAnimation(animation);
        Thread timer= new Thread(){
            public void run() {
                try {
                    sleep(3000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
