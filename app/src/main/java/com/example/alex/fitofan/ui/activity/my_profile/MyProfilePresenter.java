package com.example.alex.fitofan.ui.activity.my_profile;

public class MyProfilePresenter implements MyProfileContract.EventListener{

    private MyProfileContract.View view;

    MyProfilePresenter(MyProfileContract.View view) {
        this.view = view;
    }

}
