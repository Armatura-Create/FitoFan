package com.example.alex.fitofan.ui.activity.preview_plan;


class CreatePlanPresenter implements PreviewPlanContract.EventListener {

    private PreviewPlanContract.View view;

    CreatePlanPresenter(PreviewPlanContract.View view) {
        this.view = view;
    }

}