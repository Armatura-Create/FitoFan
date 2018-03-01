package com.example.alex.fitofan.ui.activity.MyProfile;

public class MyProfilePresenter implements MyProfileContract.EventListener{

    private MyProfileContract.View view;

    MyProfilePresenter(MyProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void onContinue(int count) {

    }


}
