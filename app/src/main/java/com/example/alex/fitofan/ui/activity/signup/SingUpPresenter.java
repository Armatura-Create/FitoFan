package com.example.alex.fitofan.ui.activity.signup;


import android.widget.Toast;

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
        view.goToSingIn();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(view.getContext(), message + "Failure", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void register(String email, String password, String firstName, String lastName) {

//        class UserLogingAsynk extends AsyncTask<Void, Void, String> {
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                HttpRequest request = new HttpRequest();
//                HashMap<String, String> params = new HashMap<>();
//                params.put("email", email);
//                params.put("name", firstName);
//                params.put("surname", firstName);
//                params.put("password", password);
//                return request.DoRequest("http://api.fitofan.com/v1/registration", params);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    if (jsonObject.getInt("status") == 1) {
//                        Log.e("onKKK: ", new Gson().toJson(s, TokenAtRegistration.class));
//                    } else {
//                        Log.e("onKKK: ", new Gson().toJson(s, TokenAtRegistration.class));
//                    }
//                } catch (Exception ex) {
//
//                    Log.e("onKKK: ", ex.toString());
//                    Log.e("onKKK: ", s);
//
//                }
//                super.onPostExecute(s);
//            }
//        }
//        new UserLogingAsynk().execute();

        if (Connection.isNetworkAvailable(view.getContext())) {
//            RegisterModel model = new RegisterModel();
//            model.setName(firstName);
//            model.setEmail(email);
//            model.setSurname(firstName);
//            model.setPassword(password);
            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("name", firstName);
            params.put("surname", "");
            params.put("password", password);
            Request.getInstance().singUp(params, this);
        } else onFailure(CONNECTION_ERROR);
    }

    @Override
    public void goToSingIn() {
        view.goToSingIn();
    }

}