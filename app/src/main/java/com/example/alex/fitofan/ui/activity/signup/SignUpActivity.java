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
        initListeners();
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mBinding.content.btRegister.setOnClickListener(v -> presenter.register("1", "1", "1","1"));
        mBinding.content.btBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void goToSingIn() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}