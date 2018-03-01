package com.example.alex.fitofan.ui.activity.MyProfile;

import android.content.Context;

public interface MyProfileContract {

    interface View {
        Context getContext();

    }

    interface EventListener{
        void onContinue(int count);
    }

}
