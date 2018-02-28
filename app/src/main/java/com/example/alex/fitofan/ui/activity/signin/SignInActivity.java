package com.example.alex.fitofan.ui.activity.signin;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivitySignInBinding;
import com.example.alex.fitofan.ui.activity.forgot_password.ForgotPasswordActivity;
import com.example.alex.fitofan.ui.activity.main.MainActivity;
import com.example.alex.fitofan.ui.activity.signup.SignUpActivity;

//TODO All general Activities HAVE TO BE DONE! with MVC/MVP pattern (ex. signup package)
//TODO Все основные классы активити обязательно делать в стиле MVC/MVP паттерна (пример signup package)
public class SignInActivity extends AppCompatActivity implements SignInContract.View {

    private ActivitySignInBinding binding;
    private SignInPresenter presenter;

    private static final String ENTERING_ERROR = "Entering error!";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        presenter = new SignInPresenter(this, binding);
        presenter.loginWithFB();
    }

    @Override
    public void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        presenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void goToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void forgotPassword() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    @Override
    public void goToLogin() {
        startActivity(new Intent(this, SignInActivity.class));
    }
}
