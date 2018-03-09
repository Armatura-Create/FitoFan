package com.example.alex.fitofan.ui.activity.user_profile;


class UserProfilePresenter implements UserProfileContract.EventListener {

    private UserProfileContract.View view;

    UserProfilePresenter(UserProfileContract.View view) {
        this.view = view;
    }


    @Override
    public void register(String name, String email, String password, String phone) {
    }

    @Override
    public void goToMain() {
        view.goToMain();
    }

}