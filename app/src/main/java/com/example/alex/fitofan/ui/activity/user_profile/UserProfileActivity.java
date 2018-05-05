package com.example.alex.fitofan.ui.activity.user_profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityProfileUserBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.ILoadingStatusUserPlans;
import com.example.alex.fitofan.interfaces.ILoadingStatusWallPlans;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.models.GetPlansModel;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.GetWallModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.View, ILoadingStatus<User>, ILoadingStatusUserPlans, LikeStatus {

    //TODO: It might be useful to use DataBinding
    //TODO: Будет хорошо, если вы будете использовать DataBinding
    private ActivityProfileUserBinding mBinding;
    private UserProfilePresenter presenter;
    private RecyclerAdapterUserProfile adapter;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_user);
        presenter = new UserProfilePresenter(this);
        initListeners();
        initRecycler();
        initRequest();
    }

    private void initRequest() {
        if (Connection.isNetworkAvailable(this)) {
            mUser = new User();
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", getIntent().getStringExtra("uid"));
            Request.getInstance().getUserData(map, this);

            HashMap<String, String> map2 = new HashMap<>();
            map2.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map2.put("user_id", getIntent().getStringExtra("uid"));
            map2.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            Request.getInstance().getUserPlans(map2, this);
        }
    }

    private void initRecycler() {
        ArrayList<GetTrainingModel> models = new ArrayList<>();
        mBinding.content.rvUserProfile.setNestedScrollingEnabled(false);
        mBinding.content.rvUserProfile.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rvUserProfile.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterUserProfile(this, mUser, models);
        mBinding.content.rvUserProfile.setAdapter(adapter);
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    protected void goPreviewPlan(String planId) {
        Intent intent = new Intent(getContext(), PreviewPlanActivity.class);
        intent.putExtra("planId", planId);
        intent.putExtra("isWall", true);
        startActivity(intent);
    }

    protected void likePlan(String id){
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        Request.getInstance().like(map, this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onSuccess(User info) {
        mUser = info;
        adapter.setmUserModel(mUser);
        adapter.notifyDataSetChanged();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(info.getName() + " " + info.getSurname());
    }

    @Override
    public void onSuccess(GetPlansModel info) {
        adapter.setmWallModels(info.getTrainings());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(Boolean info) {
        Toast.makeText(getContext(), info + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String message) {

    }
}