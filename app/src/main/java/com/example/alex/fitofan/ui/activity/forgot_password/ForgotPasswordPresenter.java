package com.example.alex.fitofan.ui.activity.forgot_password;

import com.example.alex.fitofan.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordPresenter implements ForgotPasswordContact.EventListener{

    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordContact.View view;

    ForgotPasswordPresenter(ForgotPasswordContact.View view, ActivityForgotPasswordBinding binding) {
        this.binding = binding;
        this.view = view;
    }

    @Override
    public void onContinue(int count) {

    }


}
