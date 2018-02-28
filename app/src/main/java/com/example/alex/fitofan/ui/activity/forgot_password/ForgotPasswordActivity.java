package com.example.alex.fitofan.ui.activity.forgot_password;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordContact.View {

    private ActivityForgotPasswordBinding binding;

    private ForgotPasswordPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);

        presenter = new ForgotPasswordPresenter(this, binding);

//        binding.toolbarForgotPassword.setNavigationOnClickListener(view -> onBackPressed());


    }

    @Override
    public Context getContext() {
        return this;
    }


}
