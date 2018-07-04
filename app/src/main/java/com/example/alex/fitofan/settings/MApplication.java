package com.example.alex.fitofan.settings;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Resources;

@SuppressLint("Registered")
public class MApplication extends Application {

    private Resources res;
    private static MApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        res = getResources();
    }

    public static MApplication getInstance() {
        return instance;
    }

    /**
     * быстрый доступ к ресурсам приложения
     */
    public Resources getMResources() {
        return res;
    }
}
