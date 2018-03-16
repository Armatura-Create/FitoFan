package com.example.alex.fitofan.ui.activity.preview_plan;


class PreviewPlanPresenter implements PreviewPlanContract.EventListener {

    private PreviewPlanContract.View view;

    PreviewPlanPresenter(PreviewPlanContract.View view) {
        this.view = view;
    }

}