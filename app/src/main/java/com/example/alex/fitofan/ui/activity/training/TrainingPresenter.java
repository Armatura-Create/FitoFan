package com.example.alex.fitofan.ui.activity.training;

import android.app.Dialog;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.utils.CustomDialog;

public class TrainingPresenter implements TrainingContact.EventListener {

    private TrainingContact.View view;

    TrainingPresenter(TrainingContact.View view) {
        this.view = view;
    }

    @Override
    public void close() {
        Dialog dialog = CustomDialog.dialogSimple(view.getContext(), "Close training", "Are you sure?", "Yes", "No");
        dialog.findViewById(R.id.bt_negative).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_positive).setOnClickListener(v -> view.close());
    }
}
