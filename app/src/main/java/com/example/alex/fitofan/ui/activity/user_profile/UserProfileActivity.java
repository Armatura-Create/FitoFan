package com.example.alex.fitofan.ui.activity.user_profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityProfileUserBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.ILoadingStatusUserPlans;
import com.example.alex.fitofan.interfaces.ILoadingStatusWallPlans;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.interfaces.SubStatus;
import com.example.alex.fitofan.models.GetPlansModel;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.GetWallModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.ui.activity.sub.SubActivity;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.View, ILoadingStatus<User>, ILoadingStatusUserPlans, LikeStatus, SubStatus {

    //TODO: It might be useful to use DataBinding
    //TODO: Будет хорошо, если вы будете использовать DataBinding
    private ActivityProfileUserBinding mBinding;
    private UserProfilePresenter presenter;
    private RecyclerAdapterUserProfile adapter;
    private User mUser;
    private boolean isSub;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_user);
        presenter = new UserProfilePresenter(this);
        setSupportActionBar(mBinding.toolbar);
        initListeners();
        initRecycler();
        initRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (getIntent().getStringExtra("uid").equals(
                new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
        )) {
            return true;
        }
        getMenuInflater().inflate(R.menu.user_profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_sub) {
            if (Connection.isNetworkAvailable(getContext())) {
                if (isSub) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                    map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                    map.put("user_id", mUser.getUid());
                    Request.getInstance().unSubscribeUser(map, this);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                    map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                    map.put("user_id", mUser.getUid());
                    Request.getInstance().subscribeUser(map, this);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRequest() {
        if (Connection.isNetworkAvailable(this)) {
            mUser = new User();
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", getIntent().getStringExtra("uid"));
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
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

    protected void goPreviewPlan(String planId, String userId) {
        Intent intent = new Intent(getContext(), PreviewPlanActivity.class);
        intent.putExtra("planId", planId);
        intent.putExtra("isWall", true);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    protected void goSub() {
        Intent intent = new Intent(this, SubActivity.class);
        intent.putExtra("user_id", getIntent().getStringExtra("uid"));
        startActivity(intent);
    }

    protected void likePlan(String id) {
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
        if (!new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
                .equals(getIntent().getStringExtra("uid"))
                ) {
            if (info.getSubscribed() == 1) {
                isSub = true;
                menu.getItem(0).setTitle(getResources().getString(R.string.unsubscribe));
            }
        }
        mUser = info;
        adapter.setmUserModel(info);
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
    public void onSuccess(String info) {
        if (info.equals("true")) {
            menu.getItem(0).setTitle(getResources().getString(R.string.unsubscribe));
            isSub = true;
        }
        if (info.equals("false")) {
            menu.getItem(0).setTitle(getResources().getString(R.string.subscribe));
            isSub = false;
        }
    }

    @Override
    public void onFailure(String message) {

    }
}