package com.example.alex.fitofan.ui.activity.main;


import android.support.v4.app.Fragment;

import java.util.HashMap;

public class MainPresenter implements MainContract.EventListener {

    private MainContract.View view;

    private HashMap<String, Fragment> frMap;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

}
