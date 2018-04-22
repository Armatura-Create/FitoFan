package com.example.alex.fitofan.ui.activity.signup;

import android.content.Context;

import com.example.alex.fitofan.interfaces.ILoadingStatus;

public class SingUpContract {

    interface View {

        Context getContext();

        void goToSingIn();

        void goToMain();

    }

    interface EventListener extends ILoadingStatus<String> {

        void register(String name, String email, String password, String phone);

        void goToSingIn();
    }
}
