package com.example.alex.fitofan.ui.activity.signin;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivitySignInBinding;
import com.example.alex.fitofan.models.LocationModel;
import com.example.alex.fitofan.models.SingInModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.HashMap;

class SignInPresenter implements SignInContract.EventListener {
    private SignInContract.View view;
    private CallbackManager mCallbackManager;
    private ActivitySignInBinding binding;


    SignInPresenter(SignInContract.View view, ActivitySignInBinding binding) {
        this.view = view;
        this.binding = binding;
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void signIn(String password, String email) {
//        SingInModel model = new SingInModel();
//        model.setEmail(email);
//        model.setPassword(password);
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        SingInModel model = new SingInModel();
        model.setPassword(password);
        model.setEmail(email);

        Request.getInstance().singIn(params, this);
    }

    public void loginWithFB() {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newGraphPathRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + loginResult.getAccessToken().getUserId() + "?fields=location,email",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                try {
                                    if (!response.getJSONObject().isNull("location") && !response.getJSONObject().isNull("email")) {
                                        Log.e("onCompleted: ", response.getJSONObject().get("email").toString());
                                        String resp = response.getJSONObject().get("location").toString();
                                        Log.e("onCompleted: ", new Gson().fromJson(resp, LocationModel.class)
                                                .getCity());
                                        loginUserWithFB(loginResult.getAccessToken().getUserId(),
                                                new Gson().fromJson(resp, LocationModel.class)
                                                        .getCity(),
                                                response.getJSONObject().get("email").toString());
                                    } else {
                                        loginUserWithFB(loginResult.getAccessToken().getUserId(),
                                                "", "");
                                    }
                                } catch (JSONException e) {
                                    Log.e("onCompleted: ", e.toString());
                                    LoginManager.getInstance().logOut();
                                    e.printStackTrace();
                                }

                            }
                        });

                request.executeAsync();
                Log.e("Facebook", "All is ok. Token: " + loginResult.getAccessToken().getToken());
//                Toast.makeText(view.getContext(), "All is ok. Token: " + loginResult.getAccessToken().getToken(), Toast.LENGTH_SHORT).show();
                //Сохраняем токен при регистрации у себя на телефоне
                MSharedPreferences.getInstance().setFbToken(loginResult.getAccessToken().getToken());
                //TODO start home_activity if phone exist and enter_phone_activity if not
            }

            @Override
            public void onCancel() {
                Log.e("Facebook", "Canceled");
                Toast.makeText(view.getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Facebook", "Error: " + error.getMessage());
//                Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUserWithFB(String id, String city, String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500L);
                    for (; ; ) {
                        if (Profile.getCurrentProfile().getFirstName() != null) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("name", Profile.getCurrentProfile().getFirstName());
                            params.put("surname", Profile.getCurrentProfile().getLastName());
                            params.put("image", String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(800, 640)));
                            params.put("location", city);
                            params.put("email", email);
                            params.put("facebook_id", id);
                            Request.getInstance().singInWithFB(params, SignInPresenter.this);
                            return;
                        }
                    }
                } catch (Exception e) {
                    LoginManager.getInstance().logOut();
                    Answers.getInstance().logLogin(new LoginEvent()
                            .putMethod("ErrorLoginFB")
                            .putSuccess(false));
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }

            }
        }).start();
    }

    void goToForgotPass() {
        view.goToForgotPass();
    }

    void goToRegistration() {
        view.goToSignUp();
    }

    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }

    @Override
    public void onSuccess(String info) {
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Answers.getInstance().logLogin(new LoginEvent()
                .putMethod("LoginFBorEmail")
                .putSuccess(true));

        new Thread(() -> {
            try {
                Thread.sleep(1500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            view.goToMain();
        }).start();

    }

    @Override
    public void onFailure(String message) {
        if (message.equals("incorrect")) {
            Toast.makeText(view.getContext(), view.getContext().getResources().getString(R.string.incorrect_email_or_pass), Toast.LENGTH_SHORT).show();
            Answers.getInstance().logLogin(new LoginEvent()
                    .putMethod("Incorrect")
                    .putSuccess(false));
        }
        Answers.getInstance().logLogin(new LoginEvent()
                .putMethod("ErrorLoginEmail")
                .putSuccess(false));
    }
}
