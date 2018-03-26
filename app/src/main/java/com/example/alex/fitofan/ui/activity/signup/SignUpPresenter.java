package com.example.alex.fitofan.ui.activity.signup;


import android.widget.Toast;

class SignUpPresenter implements SignUpContract.EventListener {

    private SignUpContract.View view;

    SignUpPresenter(SignUpContract.View view) {
        this.view = view;
    }


    @Override
    public void register(String name, String email, String password, String phone) {
        Toast.makeText(view.getContext(), "Register", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToSingIn() {
        view.goToSingIn();
    }

}