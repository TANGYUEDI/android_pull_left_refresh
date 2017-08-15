package com.example.yixiang.testdemoapplication;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    View viewAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewAlpha = findViewById(R.id.viewAlpha);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(viewAlpha, "alpha", 0f, 0.5f, 0f);
                        animator.setDuration(10);
                        animator.start();
                    }
                });
            }
        }).start();

    }
}
