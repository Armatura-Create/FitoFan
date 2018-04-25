package com.example.alex.fitofan.ui.activity.launch_screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.UserDataModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.main.MainActivity;
import com.example.alex.fitofan.ui.activity.signin.SignInActivity;
import com.google.gson.Gson;

import java.util.HashMap;

public class LaunchScreenActivity extends AppCompatActivity implements ILoadingStatus {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MSharedPreferences.getInstance().getUserInfo() != null &&
                new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class) != null
                && new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser() != null) {
            GetUserModel model = new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class);
            HashMap<String, String> params = new HashMap<>();
            params.put("uid", model.getUser().getUid());
            params.put("signature", model.getUser().getSignature());
            Request.getInstance().singIn(params, this);
        } else {
            goSingIn();
        }

    }

    private void goSingIn() {
        handler = new Handler();
        runnable = () -> {
            startActivity(new Intent(LaunchScreenActivity.this, SignInActivity.class));
            overridePendingTransition(R.anim.abc_slide_in_bottom, android.R.anim.fade_out);
            finish();
        };

        setContentView(R.layout.activity_launch_screen);

        // переход на другое активити спустя 3 сек
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onSuccess(Object info) {
        handler = new Handler();
        runnable = () -> {
            startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.abc_slide_in_bottom, android.R.anim.fade_out);
            finish();
        };

        setContentView(R.layout.activity_launch_screen);

        // переход на другое активити спустя 3 сек
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onFailure(String message) {
        goSingIn();
    }
}