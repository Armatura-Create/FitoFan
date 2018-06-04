package com.example.alex.fitofan.utils;

import com.example.alex.fitofan.settings.MApplication;
import com.example.alex.fitofan.R;

public abstract class StaticValues {

    //for alerts
    public static final String CONNECTION_ERROR = MApplication.getInstance().getResources().getString(R.string.connection_error);
    public static final String CHECK_YOUR_CONNECTION = "please check your internet connection";
    public static final String GO_TO_SETTINGS = "Go to settings";
    public static final String REGISTRATION_ERROR = "Registration error";
    public static final String TRY_AGAIN = "Try again";

    //for type
    public static final int TIME = 0;
    public static final int DISTANCE = 1;
    public static final int WEIGHT = 2;
    public static final int COUNT = 3;

}
