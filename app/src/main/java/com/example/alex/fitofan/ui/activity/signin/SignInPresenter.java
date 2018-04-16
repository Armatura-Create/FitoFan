package com.example.alex.fitofan.ui.activity.signin;

import android.util.Log;
import android.widget.Toast;

import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivitySignInBinding;
import com.example.alex.fitofan.models.SingInModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

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
        SingInModel model = new SingInModel();
        model.setEmail(email);
        model.setPassword(password);

        Request.getInstance().singIn(model, this);
    }

    public void loginWithFB() {
        Log.e("Facebook", "It was started");
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("Facebook", "All is ok. Token: " + loginResult.getAccessToken().getToken());
//                Toast.makeText(view.getContext(), "All is ok. Token: " + loginResult.getAccessToken().getToken(), Toast.LENGTH_SHORT).show();
                loginUserWithFB(loginResult.getAccessToken().getToken());

                //Сохраняем токен при регистрации у себя на телефоне
//                MSharedPreferences.getInstance().setKey(loginResult.getAccessToken().getToken());
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
                Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUserWithFB(String fbToken) {
        Log.e("user_info", "Send FB token");
    }

    void goToMain() {
        view.goToMain();
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
        Toast.makeText(view.getContext(), info + "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(view.getContext(), message + "Failure", Toast.LENGTH_SHORT).show();
    }
}
