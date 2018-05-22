package com.example.alex.fitofan.utils.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;

import java.util.Objects;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

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

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                break;
            case 2:
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }

        tvClose.setOnClickListener(v -> mDialog.dismiss());

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

    public static Dialog card(Context context, Window window, String title, String description, String image) {
        mDialog = new Dialog(context);

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_card, null);
//        layout.setMinimumWidth((int)(displayRectangle.width() * 0.7f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.8f));
        mDialog.setContentView(layout);
        TextView tvTitle = mDialog.findViewById(R.id.name_exercise);
        TextView tvDescription = mDialog.findViewById(R.id.description_exercise);
        ImageView imageExercise = mDialog.findViewById(R.id.image_exercise);
        ImageView close = mDialog.findViewById(R.id.close_dialog);


        tvTitle.setText(title);
        tvDescription.setText(description);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        if (image != null) {
            Glide.with(context) //передаем контекст приложения
                    .load(Uri.parse(image))
                    .apply(centerCropTransform())
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .transition(withCrossFade())
                    .into(imageExercise); //ссылка на ImageView
        } else {
            Glide.with(context)
                    .load(R.drawable.background_launch_screen)
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .into(imageExercise);
        }

        close.setOnClickListener(v -> mDialog.dismiss());

        mDialog.setCancelable(true);

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

    public static void cardSet(Dialog dialog, String title, String description, String image) {
        TextView tvTitle = dialog.findViewById(R.id.name_exercise);
        TextView tvDescription = dialog.findViewById(R.id.description_exercise);
        ImageView imageExercise = dialog.findViewById(R.id.image_exercise);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        if (image != null) {
            Glide.with(dialog.getContext()) //передаем контекст приложения
                    .load(Uri.parse(image))
                    .apply(centerCropTransform())
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .transition(withCrossFade())
                    .into(imageExercise); //ссылка на ImageView
        } else {
            Glide.with(dialog.getContext())
                    .load(R.drawable.background_launch_screen)
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .into(imageExercise);
        }

    }
}
