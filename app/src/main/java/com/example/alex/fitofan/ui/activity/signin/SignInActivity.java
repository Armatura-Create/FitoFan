package com.example.alex.fitofan.ui.activity.signin;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivitySignInBinding;
import com.example.alex.fitofan.ui.activity.forgot_password.ForgotPasswordActivity;
import com.example.alex.fitofan.ui.activity.main.MainActivity;
import com.example.alex.fitofan.ui.activity.signup.SingUpActivity;
import com.example.alex.fitofan.utils.CheckerInputData;

import java.util.Arrays;
import java.util.List;

//TODO All general Activities HAVE TO BE DONE! with MVC/MVP pattern (ex. signup package)
//TODO Все основные классы активити обязательно делать в стиле MVC/MVP паттерна (пример signup package)
public class SignInActivity extends AppCompatActivity implements SignInContract.View {

    private ActivitySignInBinding mBinding;
    private SignInPresenter presenter;

    private static final String ENTERING_ERROR = "Entering error!";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        presenter = new SignInPresenter(this, mBinding);
//        presenter.loginWithFB();
        initListeners();
    }

    private void initListeners() {
        mBinding.btLogin.setOnClickListener(v -> {
            boolean isEmpty = false;
            List<EditText> list = Arrays.asList(
                    mBinding.login,
                    mBinding.password
            );
            for (EditText edit : list) {
                if (TextUtils.isEmpty(edit.getText().toString().trim())) {
                    edit.setError("Обязательное поле");
                    isEmpty = true;
                }
            }

            if (isEmpty) return;
            if (!CheckerInputData.isEmail(mBinding.login.getText().toString().trim())) {
                Toast.makeText(this, "Логин не валидный.\nExample:example@gmail.com", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!CheckerInputData.isPassword(mBinding.password.getText().toString().trim())) {
                Toast.makeText(this, "Пароль должен содержать не менее 8 символов", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.signIn(mBinding.password.getText().toString().trim(), mBinding.login.getText().toString().trim());
        });
        mBinding.forgotPass.setOnClickListener(v -> presenter.goToForgotPass());
        mBinding.registration.setOnClickListener(v -> presenter.goToRegistration());


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
        startActivity(new Intent(this, SingUpActivity.class));
    }

    @Override
    public void goToLogin() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    @Override
    public void goToForgotPass() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }
}
