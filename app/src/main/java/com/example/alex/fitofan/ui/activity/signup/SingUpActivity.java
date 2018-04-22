package com.example.alex.fitofan.ui.activity.signup;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivitySignUpBinding;
import com.example.alex.fitofan.ui.activity.main.MainActivity;
import com.example.alex.fitofan.ui.activity.signin.SignInActivity;
import com.example.alex.fitofan.utils.CheckerInputData;

import java.util.Arrays;
import java.util.List;

public class SingUpActivity extends AppCompatActivity implements SingUpContract.View {

    //TODO: It might be useful to use DataBinding
    //TODO: Будет хорошо, если вы будете использовать DataBinding
    private ActivitySignUpBinding mBinding;
    private SingUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        presenter = new SingUpPresenter(this);
        initListeners();
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mBinding.content.btBack.setOnClickListener(v -> onBackPressed());
        mBinding.content.btRegister.setOnClickListener(view -> {
            boolean isEmpty = false;
            List<EditText> list = Arrays.asList(
                    mBinding.content.login,
                    mBinding.content.firstName,
                    mBinding.content.password,
                    mBinding.content.passwordAgain
//                        mBinding.content.surname
            );
            for (EditText edit : list) {
                if (TextUtils.isEmpty(edit.getText().toString().trim())) {
                    edit.setError("Обязательное поле");
                    isEmpty = true;
                }
            }
            if (isEmpty) return;

            if (!CheckerInputData.isEmail(mBinding.content.login.getText().toString().trim())) {
                Toast.makeText(SingUpActivity.this, "Логин не валидный.\nExample:example@gmail.com", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!CheckerInputData.isPassword(mBinding.content.password.getText().toString().trim())) {
                Toast.makeText(SingUpActivity.this, "Пароль должен содержать не менее 8 символов, цифры, буквы верхнего и нижнего регистра", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!mBinding.content.password.getText().toString().trim().equals(mBinding.content.passwordAgain.getText().toString().trim())) {
                Toast.makeText(SingUpActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.register(mBinding.content.login.getText().toString().trim(),
                    mBinding.content.password.getText().toString().trim(),
                    mBinding.content.firstName.getText().toString().trim(),
                    mBinding.content.firstName.getText().toString().trim());
        });
    }

    @Override
    public void goToSingIn() {
        onBackPressed();
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