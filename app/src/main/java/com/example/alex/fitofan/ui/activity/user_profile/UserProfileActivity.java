package com.example.alex.fitofan.ui.activity.user_profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityProfileUserBinding;
import com.example.alex.fitofan.ui.activity.main.MainActivity;

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.View {

    //TODO: It might be useful to use DataBinding
    //TODO: Будет хорошо, если вы будете использовать DataBinding
    private ActivityProfileUserBinding mBinding;
    private UserProfilePresenter presenter;
    private RecyclerAdapterUserProfile adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_user);
        presenter = new UserProfilePresenter(this);
        initListeners();
        initRecycler();
    }

    private void initRecycler() {
        mBinding.content.rvUserProfile.setNestedScrollingEnabled(false);
        mBinding.content.rvUserProfile.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rvUserProfile.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterUserProfile(this);
        mBinding.content.rvUserProfile.setAdapter(adapter);
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public Context getContext() {
        return this;
    }
}