package com.example.alex.fitofan.settings;

import android.content.Context;
import android.content.SharedPreferences;

//Не желательно использовать как БД
//Только как класс для хранения мелких настроек приложения
public class MSharedPreferences {

    private static final String SHARED_PACKAGE = "user_info";
    private static final String TRAINING_PLANS = "training_plans";

    private static MSharedPreferences loader;
    private SharedPreferences sharedPref;

    private MSharedPreferences() {
        sharedPref = MApplication.getInstance().getApplicationContext().getSharedPreferences(SHARED_PACKAGE, Context.MODE_PRIVATE);
    }

    public static MSharedPreferences getInstance() {
        if (loader == null) loader = new MSharedPreferences();
        return loader;
    }

    public String getPlans() {
        return sharedPref.getString(TRAINING_PLANS, null);
    }

    public void setPlans(String plans) {
        sharedPref.edit().putString(TRAINING_PLANS, plans).apply();
    }
}
