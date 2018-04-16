package com.example.alex.fitofan.ui.activity.signup;


import android.widget.Toast;

import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.models.RegisterModel;
import com.example.alex.fitofan.utils.Connection;

import static com.example.alex.fitofan.utils.StaticValues.CONNECTION_ERROR;

class SingUpPresenter implements SingUpContract.EventListener {

    private SingUpContract.View view;

    SingUpPresenter(SingUpContract.View view) {
        this.view = view;
    }

    @Override
    public void onSuccess(String info) {
        Toast.makeText(view.getContext(), info + "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(view.getContext(), message + "Failure", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void register(String email, String password, String firstName, String lastName) {
        if (Connection.isNetworkAvailable(view.getContext())) {
            RegisterModel model = new RegisterModel();
            model.setName(firstName);
            model.setEmail(email);
            model.setSurname(firstName);
            model.setPassword(password);
            Request.getInstance().singUp(model, this);
        } else onFailure(CONNECTION_ERROR);
    }

    @Override
    public void goToSingIn() {
        view.goToSingIn();
    }

}