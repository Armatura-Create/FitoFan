package com.example.alex.fitofan.ui.activity.comments;


public class CommentsPresenter implements CommentsContract.EventListener {

    private CommentsContract.View view;

    public CommentsPresenter(CommentsContract.View view) {
        this.view = view;
    }

}
