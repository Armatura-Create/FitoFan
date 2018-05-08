package com.example.alex.fitofan.ui.activity.comments;


import android.app.Dialog;
import android.content.Intent;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.utils.CustomDialog;

public class CommentsPresenter implements CommentsContract.EventListener {

    private CommentsContract.View view;

    public CommentsPresenter(CommentsContract.View view) {
        this.view = view;
    }

}
