package com.example.alex.fitofan.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.example.alex.fitofan.ui.activity.signin.SignInActivity;

/**
 * Created by 0x008800 on 01.12.17.
 * this Class had been made for simple AlertDialogs
 */

public final class SimpleAlert {

    private static AlertDialog alertDialog = null;

    private SimpleAlert() {
    }

    public static void show(Context context, String title, String message, String button) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, button, (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static void showNoConnection(Context context){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(StaticValues.CONNECTION_ERROR);
        alertDialog.setMessage(StaticValues.CHECK_YOUR_CONNECTION);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, StaticValues.TRY_AGAIN, (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, StaticValues.GO_TO_SETTINGS, (dialogInterface, i) -> {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
            dialogInterface.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
