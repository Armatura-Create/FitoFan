package com.example.alex.fitofan.utils.CustomDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.ui.activity.FullScreenImage;

import java.util.ArrayList;
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
//                et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
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

    public static Dialog card(Context context, Window window, String title, String description, ArrayList<String> images) {
        mDialog = new Dialog(context);

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_card, null);
//        layout.setMinimumWidth((int)(displayRectangle.width() * 0.7f));
        layout.setMinimumHeight((int) (displayRectangle.height() * 0.77f));
        mDialog.setContentView(layout);
        TextView tvTitle = mDialog.findViewById(R.id.name_exercise);
        TextView tvDescription = mDialog.findViewById(R.id.description_exercise);
        ImageView close = mDialog.findViewById(R.id.close_dialog);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        if (images != null) {
            ViewPager viewPager = mDialog.findViewById(R.id.pager_image);
            ViewPagerImageAdapter pagerImageAdapter = new ViewPagerImageAdapter(context, images);
            viewPager.setAdapter(pagerImageAdapter);
        }
        close.setOnClickListener(v -> mDialog.dismiss());

        mDialog.setCancelable(true);

        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();

        return mDialog;
    }

    public static Dialog cardMusic(Context context, String title, String image) {
        mDialog = new Dialog(context);

        mDialog.setContentView(R.layout.dialog_music);
        TextView tvTitle = mDialog.findViewById(R.id.name_exercise);
        ImageView imageExercise = mDialog.findViewById(R.id.image_exercise);
        ImageView close = mDialog.findViewById(R.id.close_dialog);


        tvTitle.setText(title);
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

    public static void cardSetMusic(Dialog dialog, String title, String image, boolean isAudio) {
        TextView tvTitle = dialog.findViewById(R.id.name_exercise);
        ImageView imageExercise = dialog.findViewById(R.id.image_exercise);
        LinearLayout border = dialog.findViewById(R.id.background_border_audio);

        if(isAudio){
            border.setVisibility(View.VISIBLE);
        } else {
            border.setVisibility(View.INVISIBLE);
        }
        tvTitle.setText(title);
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

    public static void cardSet(Dialog dialog, String title, String description, ArrayList<String> images) {
        TextView tvTitle = dialog.findViewById(R.id.name_exercise);
        TextView tvDescription = dialog.findViewById(R.id.description_exercise);
        ImageView imageExercise = dialog.findViewById(R.id.image_exercise);

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        if (images != null) {
            ViewPager viewPager = mDialog.findViewById(R.id.pager_image);
            ViewPagerImageAdapter pagerImageAdapter = new ViewPagerImageAdapter(dialog.getContext(), images);
            viewPager.setAdapter(pagerImageAdapter);
        }

    }
}
