package com.example.alex.fitofan.ui.activity.training;

import android.app.Dialog;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.utils.CustomDialog;

public class TrainingPresenter implements TrainingContact.EventListener {

    private TrainingContact.View view;

    TrainingPresenter(TrainingContact.View view) {
        this.view = view;
    }

}
