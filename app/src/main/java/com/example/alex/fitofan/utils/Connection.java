package com.example.alex.fitofan.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class Connection {
    /***
     * метод для проверки доступа к сети
     * @param context Activity context
     * @return whether network is available or not
     */
    public static boolean isNetworkAvailable(View view, Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            Snackbar.make(view, "No connection", Snackbar.LENGTH_LONG)
                    .setAction("Settings", v -> context.startActivity(new Intent(Settings.ACTION_SETTINGS)))
                    .show();
            return false;
        }
        Snackbar.make(view, "No connection", Snackbar.LENGTH_LONG)
                .setAction("Settings", v -> context.startActivity(new Intent(Settings.ACTION_SETTINGS)))
                .show();
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show();
        return false;
    }
}
