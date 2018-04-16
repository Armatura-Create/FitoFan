package com.example.alex.fitofan.ui.activity.signin;

import android.content.Context;

import com.example.alex.fitofan.interfaces.ILoadingStatus;

interface SignInContract {

    interface View {
        Context getContext();

        void goToSignUp();

        void goToMain();

        void goToLogin();

        void goToForgotPass();
    }

    interface EventListener extends ILoadingStatus<String> {

        void signIn(String password, String phone_number);
    }

}
