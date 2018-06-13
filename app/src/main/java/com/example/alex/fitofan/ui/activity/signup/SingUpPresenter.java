package com.example.alex.fitofan.ui.activity.signup;


import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.utils.Connection;

import java.util.HashMap;

import static com.example.alex.fitofan.utils.StaticValues.CONNECTION_ERROR;

class SingUpPresenter implements SingUpContract.EventListener {

    private SingUpContract.View view;

    SingUpPresenter(SingUpContract.View view) {
        this.view = view;
    }

    @Override
    public void onSuccess(String info) {
        Answers.getInstance().logSignUp(new SignUpEvent()
                .putMethod("SingUp")
                .putSuccess(true));
        Toast.makeText(view.getContext(), "Registered", Toast.LENGTH_SHORT).show();
        view.goToSingIn();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(view.getContext(), message + "Failure", Toast.LENGTH_SHORT).show();
        Answers.getInstance().logSignUp(new SignUpEvent()
                .putMethod("SingUpError" + message)
                .putSuccess(false));
    }


    @Override
    public void register(String email, String password, String firstName) {

        if (Connection.isNetworkAvailable(view.getContext())) {
            String[] name = firstName.split(" ", 2);

            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("name", name[0]);
            if (name.length > 1) {
                if (name[1] != null && !name[1].equals(""))
                    params.put("surname", name[1]);
                else
                    params.put("surname", " ");
            } else
                params.put("surname", " ");
            params.put("password", password);
            Request.getInstance().singUp(params, this);
        } else onFailure(CONNECTION_ERROR);
    }

    @Override
    public void goToSingIn() {
        view.goToSingIn();
    }

}