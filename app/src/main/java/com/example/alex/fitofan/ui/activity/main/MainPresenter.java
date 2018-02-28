package com.example.alex.fitofan.ui.activity.main;


public class MainPresenter implements MainContract.EventListener {

    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

}
