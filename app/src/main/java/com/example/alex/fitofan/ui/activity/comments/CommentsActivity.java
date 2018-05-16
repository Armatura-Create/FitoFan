package com.example.alex.fitofan.ui.activity.comments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityCommentsBinding;
import com.example.alex.fitofan.interfaces.DelComStatus;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.SubStatus;
import com.example.alex.fitofan.models.CommentModel;
import com.example.alex.fitofan.models.GetCommentsModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.user_profile.UserProfileActivity;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class CommentsActivity extends AppCompatActivity
        implements CommentsContract.View, ILoadingStatus<GetCommentsModel>, SubStatus, DelComStatus {

    private ActivityCommentsBinding mBinding;
    private CommentsPresenter mPresenter;
    private ArrayList<CommentModel> models;
    private RecyclerAdapterComments adapter;
    private Menu menu;
    private String[] ids;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.comments_del, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove) {
            deleteComments();
        }

        if (id == R.id.action_cancel) {
            SparseBooleanArray temp = new SparseBooleanArray(adapter.getSelectedPositions().size());
            for (int i = 0; i < adapter.getSelectedPositions().size(); i++) {
                temp.put(i, false);
            }
            adapter.setSelectedPositions(temp);
            adapter.notifyDataSetChanged();
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteComments() {
        if (Connection.isNetworkAvailable(this)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] != null) {
                    map.put("comment_id", ids[i]);

                    Request.getInstance().delPlanComment(map, this);
                }
            }
        }
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
        adapter = new RecyclerAdapterComments(this, models, getIntent().getStringExtra("userId"));
        mBinding.content.rvComments.setAdapter(adapter);

    }

    public void setActionBar(SparseBooleanArray id, String[] ids) {
        this.ids = ids;
        int count = 0;
        for (int i = 0; i < id.size(); i++) {
            if (id.get(i)) {
                count++;
            }
        }
        if (count > 0) {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }


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
    public void onSuccess(Boolean info) {
        if (info) {
            adapter.getHolder().removeAt();
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
