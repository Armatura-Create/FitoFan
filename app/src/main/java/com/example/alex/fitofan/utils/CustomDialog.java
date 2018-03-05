package com.example.alex.fitofan.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.fitofan.R;

public final class CustomDialog {

    private static Dialog mDialog = null;

    private CustomDialog() {
    }

    public static Dialog dialogSimple(Context context, String title, String message, String btPositive, String btNegative) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_simple);
        TextView tvTitle = mDialog.findViewById(R.id.title_dialog_simple);
        TextView tvMassage = mDialog.findViewById(R.id.massage_dialog_simple);
        TextView tvBtPositive = mDialog.findViewById(R.id.tv_positive);
        TextView tvBtNegative = mDialog.findViewById(R.id.tv_negative);
        TextView tvClose = mDialog.findViewById(R.id.cancel_dialog);

        tvTitle.setText(title);
        tvMassage.setText(message);
        tvBtPositive.setText(btPositive);
        tvBtNegative.setText(btNegative);

        tvClose.setOnClickListener(v -> {
            mDialog.dismiss();
        });

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

    public static Dialog dialog(Context context, String title, String description, String btAdd) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog);
        TextView tvTitle = mDialog.findViewById(R.id.title_dialog_create_plan);
        TextView tvDescription = mDialog.findViewById(R.id.dialog_description);
        TextView tvBtAdd = mDialog.findViewById(R.id.tv_add_dialog);
        TextView tvClose = mDialog.findViewById(R.id.cancel_dialog);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvBtAdd.setText(btAdd);

        tvClose.setOnClickListener(v -> mDialog.dismiss());

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

    public static Dialog dialogTime(Context context, String title, String description, String btAdd) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_time);
        TextView tvTitle = mDialog.findViewById(R.id.title_dialog_create_plan);
        TextView tvDescription = mDialog.findViewById(R.id.dialog_description);
        TextView tvBtAdd = mDialog.findViewById(R.id.tv_add_dialog);
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