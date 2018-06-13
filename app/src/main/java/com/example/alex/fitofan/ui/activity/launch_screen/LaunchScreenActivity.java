package com.example.alex.fitofan.ui.activity.launch_screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.main.MainActivity;
import com.example.alex.fitofan.ui.activity.signin.SignInActivity;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;

public class LaunchScreenActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Fabric fabric = new Fabric.Builder(getApplication().getBaseContext())
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        super.onCreate(savedInstanceState);
        if (MSharedPreferences.getInstance().getUserInfo() != null &&
                new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class) != null
                && new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser() != null) {
            handler = new Handler();
            runnable = () -> {
                startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.abc_slide_in_bottom, android.R.anim.fade_out);
                finish();
            };

            setContentView(R.layout.activity_launch_screen);

            anim();

            // переход на другое активити спустя 2 сек
            handler.postDelayed(runnable, 2000);
        } else {
            goSingIn();
        }


    }

    private void anim() {
        // Получим ссылку на солнце
        ImageView sunImageView = findViewById(R.id.logo_launch);
        // Анимация для восхода солнца
        @SuppressLint("ResourceType")
        Animation animation = AnimationUtils.loadAnimation(this, R.animator.logo_down_anim);
        // Подключаем анимацию к нужному View
        sunImageView.startAnimation(animation);
    }

    private void goSingIn() {
        handler = new Handler();
        runnable = () -> {
            startActivity(new Intent(LaunchScreenActivity.this, SignInActivity.class));
            overridePendingTransition(R.anim.abc_slide_in_bottom, android.R.anim.fade_out);
            finish();
        };

        setContentView(R.layout.activity_launch_screen);

        anim();

        // переход на другое активити спустя 2 сек
        handler.postDelayed(runnable, 2000);
    }
}