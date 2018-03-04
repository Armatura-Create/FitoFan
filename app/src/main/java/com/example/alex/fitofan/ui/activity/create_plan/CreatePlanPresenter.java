package com.example.alex.fitofan.ui.activity.create_plan;


class CreatePlanPresenter implements CreatePlanContract.EventListener {

    private CreatePlanContract.View view;

    CreatePlanPresenter(CreatePlanContract.View view) {
        this.view = view;
    }

}