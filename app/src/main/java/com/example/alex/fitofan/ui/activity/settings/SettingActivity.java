package com.example.alex.fitofan.ui.activity.settings;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityForgotPasswordBinding;
import com.example.alex.fitofan.databinding.ActivitySettingsBinding;

public class SettingActivity extends AppCompatActivity implements SettingsContact.View {

    private ActivitySettingsBinding mBinding;

    private SettingsPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        presenter = new SettingsPresenter(this);

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
