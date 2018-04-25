package com.example.alex.fitofan.settings;

import android.content.Context;
import android.content.SharedPreferences;

//Не желательно использовать как БД
//Только как класс для хранения мелких настроек приложения
public class MSharedPreferences {

    private static final String SHARED_PACKAGE = "user_info";
    private static final String USER_INFO = "json_user_info";
    private static final String FB_ID = "fb_token";
    private static final String FB_TOKEN = "fb_token";

    private static MSharedPreferences loader;
    private SharedPreferences sharedPref;

    private MSharedPreferences() {
        sharedPref = MApplication.getInstance().getApplicationContext().getSharedPreferences(SHARED_PACKAGE, Context.MODE_PRIVATE);
    }

    public static MSharedPreferences getInstance() {
        if (loader == null) loader = new MSharedPreferences();
        return loader;
    }

    public void setUserInfo(String userInfo){
        sharedPref.edit().putString(USER_INFO, userInfo).apply();
    }

    public String getUserInfo() {
        return sharedPref.getString(USER_INFO, null);
    }

    public void setFbId(String id){
        sharedPref.edit().putString(FB_ID, id).apply();
    }

    public String getFbId() {
        return sharedPref.getString(FB_ID, null);
    }

    public void setFbToken(String token){
        sharedPref.edit().putString(FB_TOKEN, token).apply();
    }

    public String getFbToken() {
        return sharedPref.getString(FB_TOKEN, null);
    }
}
