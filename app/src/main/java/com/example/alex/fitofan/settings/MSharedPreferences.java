package com.example.alex.fitofan.settings;

import android.content.Context;
import android.content.SharedPreferences;

//Не желательно использовать как БД
//Только как класс для хранения мелких настроек приложения
public class MSharedPreferences {

    private static final String SHARED_PACKAGE = "user_info";
    private static final String AUTH_KEY = "key";
    private static final String BRAINTREE_TOKEN = "braintree_token";
    private static final String USER_INFO = "json_user_info";
    private static final String APP_DATA = "json_app_data";

    private static MSharedPreferences loader;
    private SharedPreferences sharedPref;

    private MSharedPreferences() {
        sharedPref = MApplication.getInstance().getApplicationContext().getSharedPreferences(SHARED_PACKAGE, Context.MODE_PRIVATE);
    }

    public static MSharedPreferences getInstance() {
        if (loader == null) loader = new MSharedPreferences();
        return loader;
    }
}
