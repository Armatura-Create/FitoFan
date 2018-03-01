package com.example.alex.fitofan.ui.activity.forgot_password;

public class ForgotPasswordPresenter implements ForgotPasswordContact.EventListener{

    private ForgotPasswordContact.View view;

    ForgotPasswordPresenter(ForgotPasswordContact.View view) {
        this.view = view;
    }

    @Override
    public void onContinue(int count) {

    }


}
