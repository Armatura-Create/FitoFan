package com.example.alex.fitofan.ui.activity.comments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityCommentsBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.SubStatus;
import com.example.alex.fitofan.models.CommentModel;
import com.example.alex.fitofan.models.GetCommentsModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.preview_plan.RecyclerAdapterPreviewPlan;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.UnpackingTraining;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class CommentsActivity extends AppCompatActivity
        implements CommentsContract.View, ILoadingStatus<GetCommentsModel>, SubStatus {

    private ActivityCommentsBinding mBinding;
    private CommentsPresenter mPresenter;
    private ArrayList<CommentModel> models;
    private RecyclerAdapterComments adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_comments);
        mPresenter = new CommentsPresenter(this);

        setSupportActionBar(mBinding.toolbar);
        initRecyclerView();
        initRequest();
        initListeners();
    }

    private void initRequest() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", getIntent().getStringExtra("planId"));
        if (Connection.isNetworkAvailable(this))
            Request.getInstance().getPlanComments(map, this);
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        mBinding.content.myImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(getContext())
                .load(new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getImage_url())
                .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .transition(withCrossFade())
                .into(mBinding.content.myImage);

        mBinding.content.sendImage.setOnClickListener(view -> {
            if (mBinding.content.inputEdit.getText().length() > 0) {
                sendComment();
                mBinding.content.inputEdit.setText(null);
            }
        });
    }

    private void sendComment() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", getIntent().getStringExtra("planId"));
        map.put("comment", mBinding.content.inputEdit.getText().toString());
        if (Connection.isNetworkAvailable(this))
            Request.getInstance().addPlanComment(map, this);
    }

    private void initRecyclerView() {

        mBinding.content.rvComments.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rvComments.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterComments(this, models);
        mBinding.content.rvComments.setAdapter(adapter);

    }

    public ArrayList<CommentModel> sort(ArrayList<CommentModel> massive) {
        for (int i = 0; i < massive.size() / 2; i++) {
            CommentModel tmp = massive.get(i);
            massive.set(i, massive.get(massive.size() - i - 1));
            massive.set(massive.size() - i - 1, tmp);
        }
        return massive;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onSuccess(GetCommentsModel info) {
        adapter.setModel(sort(info.getComments()));
        adapter.notifyDataSetChanged();
        mBinding.content.rvComments.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onSuccess(String info) {
        if (info.equals("true")) {
            initRequest();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
