package com.example.alex.fitofan.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.fitofan.R;

public final class CustomDialog {

    private static Dialog mDialog = null;

    private CustomDialog() {
    }

    public static Dialog dialogSimple(Context context, String title, String message, String btPositive, String btNegative) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_simple);
        TextView tvTitle = mDialog.findViewById(R.id.title);
        TextView tvMassage = mDialog.findViewById(R.id.massage_dialog_simple);
        Button tvBtPositive = mDialog.findViewById(R.id.bt_positive);
        Button tvBtNegative = mDialog.findViewById(R.id.bt_negative);
        TextView tvClose = mDialog.findViewById(R.id.cancel_dialog);

        tvTitle.setText(title);
        tvMassage.setText(message);
        tvBtPositive.setText(btPositive);
        tvBtNegative.setText(btNegative);

        tvClose.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        tvBtNegative.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

    public static Dialog dialog(Context context, String title, String description, String btAdd, int inputMode) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog);
        TextView tvTitle = mDialog.findViewById(R.id.title);
        TextView tvDescription = mDialog.findViewById(R.id.dialog_description);
        Button tvBtAdd = mDialog.findViewById(R.id.bt_dialog_add);
        TextView tvClose = mDialog.findViewById(R.id.cancel_dialog);
        EditText et = mDialog.findViewById(R.id.et_add_field_dialog);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvBtAdd.setText(btAdd);

        switch (inputMode) {
            case 1:
                et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
            case 2:
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }

        tvClose.setOnClickListener(v -> mDialog.dismiss());

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

    public static Dialog dialogSpinner(Context context, String title, String description, String btAdd, int inputMode) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_spiner);

        TextView tvTitle = mDialog.findViewById(R.id.title);
        TextView tvDescription = mDialog.findViewById(R.id.dialog_description);
        Button tvBtAdd = mDialog.findViewById(R.id.bt_dialog_add);
        TextView tvClose = mDialog.findViewById(R.id.cancel_dialog);
        EditText et = mDialog.findViewById(R.id.et_add_field_dialog);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvBtAdd.setText(btAdd);

        switch (inputMode) {
            case 1:
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 2:
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }

        tvClose.setOnClickListener(v -> mDialog.dismiss());

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

    public static Dialog dialogTime(Context context, String title, String description, String btAdd) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_time);
        TextView tvTitle = mDialog.findViewById(R.id.title);
        TextView tvDescription = mDialog.findViewById(R.id.dialog_description);
        Button tvBtAdd = mDialog.findViewById(R.id.bt_save_time);
        TextView tvClose = mDialog.findViewById(R.id.cancel_dialog);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvBtAdd.setText(btAdd);

        tvClose.setOnClickListener(v -> mDialog.dismiss());

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

}
