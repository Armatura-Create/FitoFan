package com.example.alex.fitofan.ui.activity.signup;

import android.content.Context;
import android.content.SharedPreferences;


class SignUpPresenter implements SignUpContract.EventListener {

    private SignUpContract.View view;

    SignUpPresenter(SignUpContract.View view) {
        this.view = view;
    }


    @Override
    public void register(String name, String email, String password, String phone) {
    }

    @Override
    public void goToMain() {
        view.goToMain();
    }

}