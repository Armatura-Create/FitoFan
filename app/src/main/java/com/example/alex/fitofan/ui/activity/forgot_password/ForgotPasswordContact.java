package com.example.alex.fitofan.ui.activity.forgot_password;

import android.content.Context;

/**
 * Created by user on 23.11.17.
 */

public interface ForgotPasswordContact {

    interface View {
        Context getContext();

    }

    interface EventListener{
        void onContinue(int count);
    }

}
