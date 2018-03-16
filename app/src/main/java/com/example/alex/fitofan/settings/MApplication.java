package com.example.alex.fitofan.settings;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Resources;

import com.example.alex.fitofan.utils.db.HelperFactory;

@SuppressLint("Registered")
public class MApplication extends Application {

    private Resources res;
    private static MApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        res = getResources();
        HelperFactory.setHelper(getApplicationContext());
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

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}
