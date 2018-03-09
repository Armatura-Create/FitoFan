package com.example.alex.fitofan.ui.activity.preview_plan;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityPlanPreviewBinding;

public class PreviewPlanActivity extends AppCompatActivity implements PreviewPlanContract.View {

    private ActivityPlanPreviewBinding mBinding;
    private CreatePlanPresenter mPresenter;
    private RecyclerAdapterPreviewPlan adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_preview);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_plan_preview);
        mPresenter = new CreatePlanPresenter(this);
        initListeners();
        initRecyclerView();
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initRecyclerView() {

        mBinding.content.rvTrainingPlan.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rvTrainingPlan.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterPreviewPlan(5, this);
        mBinding.content.rvTrainingPlan.setAdapter(adapter);

    }

    @Override
    public Context getContext() {
        return this;
    }
}