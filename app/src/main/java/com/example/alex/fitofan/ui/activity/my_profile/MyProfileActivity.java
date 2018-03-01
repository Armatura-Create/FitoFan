package com.example.alex.fitofan.ui.activity.my_profile;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityMyProfileBinding;

public class MyProfileActivity extends AppCompatActivity implements MyProfileContract.View {

    private ActivityMyProfileBinding mBinding;

    private MyProfilePresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);

        presenter = new MyProfilePresenter(this);

        initListeners();
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    @Override
    public Context getContext() {
        return this;
    }


}
