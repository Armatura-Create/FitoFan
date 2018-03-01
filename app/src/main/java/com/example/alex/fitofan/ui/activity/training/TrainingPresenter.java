package com.example.alex.fitofan.ui.activity.training;

public class TrainingPresenter implements TrainingContact.EventListener{

    private TrainingContact.View view;

    TrainingPresenter(TrainingContact.View view) {
        this.view = view;
    }

}
