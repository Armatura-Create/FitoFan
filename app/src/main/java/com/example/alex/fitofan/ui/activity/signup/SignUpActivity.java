package com.example.alex.fitofan.ui.activity.signup;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivitySignUpBinding;
import com.example.alex.fitofan.ui.activity.main.MainActivity;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    //TODO: It might be useful to use DataBinding
    //TODO: Будет хорошо, если вы будете использовать DataBinding
    private ActivitySignUpBinding mBinding;
    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        presenter = new SignUpPresenter(this);
        initLiteners();
    }

    private void initLiteners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}