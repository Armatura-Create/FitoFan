package com.example.alex.fitofan.ui.activity.signin;

import android.content.Context;

interface SignInContract {

    interface View {
        Context getContext();

        void goToSignUp();

        void goToMain();

        void forgotPassword();

        void goToLogin();
    }

    interface EventListener {

        void signIn(String password, String phone_number);
    }

}
