package com.example.alex.fitofan.ui.activity.launch_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.ui.activity.signin.SignInActivity;

public class LaunchScreenActivity extends AppCompatActivity {

    private String TAG = "LaunchScreen";

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        Intent intent = new Intent(LaunchScreenActivity.this, SignInActivity.class);

        runnable = () -> {
            startActivity(intent);
            overridePendingTransition(R.anim.abc_slide_in_bottom, android.R.anim.fade_out);
            finish();
        };

        setContentView(R.layout.activity_launch_screen);

        // переход на другое активити спустя 5 сек
        handler.postDelayed(runnable, 5000);
    }
}