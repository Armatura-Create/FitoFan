package com.example.alex.fitofan.ui.activity.user_profile;

import android.content.Context;

public class UserProfileContract {

    interface View {

        Context getContext();

        void goToMain();

    }

    interface EventListener {

        void register(String name, String email, String password, String phone);

        void goToMain();
    }
}
