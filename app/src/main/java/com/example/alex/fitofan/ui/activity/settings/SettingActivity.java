package com.example.alex.fitofan.ui.activity.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivitySettingsBinding;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.ItemClickSupport;
import com.google.gson.Gson;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingsBinding mBinding;

    private RecyclerAdapterPreviewPlan adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        initListeners();
        initRecyclerView();
    }


    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        ItemClickSupport.addTo(mBinding.rv).setOnItemClickListener((recyclerView, position, v) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(this, UserDataChangeActivity.class));
                    break;
                case 1:
                    if (new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getEmail() != null)
                        startActivity(new Intent(this, PasswordChangeActivity.class));
                    else
                        Toast.makeText(this, "У Вас не подвязана почта", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterPreviewPlan(this);
        mBinding.rv.setAdapter(adapter);

    }
}
