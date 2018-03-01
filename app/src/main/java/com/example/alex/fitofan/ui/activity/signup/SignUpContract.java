package com.example.alex.fitofan.ui.activity.signup;

import android.content.Context;

public class SignUpContract {

    interface View {

        Context getContext();

        void goToMain();

    }

    interface EventListener {

        void register(String name, String email, String password, String phone);

        void goToMain();
    }
}