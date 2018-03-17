package com.example.alex.fitofan.ui.activity.preview_plan;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityPlanPreviewBinding;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.ui.activity.training.TrainingActivity;
import com.example.alex.fitofan.utils.FormatTime;
import com.example.alex.fitofan.utils.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class PreviewPlanActivity extends AppCompatActivity implements PreviewPlanContract.View {

    private ActivityPlanPreviewBinding mBinding;
    private PreviewPlanPresenter mPresenter;
    private RecyclerAdapterPreviewPlan adapter;
    private TrainingModel mModel;
    private int isGoTo;
    private Dao<TrainingModel, Integer> mTrainings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_preview);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_plan_preview);
        mPresenter = new PreviewPlanPresenter(this);
        isGoTo = getIntent().getIntExtra("isGoTo", 0);
        getPlanFromBD(getIntent().getIntExtra("trainingModel", -1));

        initListeners();
        initRecyclerView();
    }

    private void getPlanFromBD(int trainingModel) {
        if (trainingModel >= 0) {
            try {
                mTrainings = OpenHelperManager.getHelper(this, DatabaseHelper.class).getTrainingDAO();
                assert mTrainings != null;
                mModel = mTrainings.queryForId(trainingModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            mModel = new TrainingModel();
        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initRecyclerView() {

        mBinding.content.rvTrainingPlan.setNestedScrollingEnabled(false);
        mBinding.content.rvTrainingPlan.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rvTrainingPlan.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterPreviewPlan(mModel, this, isGoTo);
        mBinding.content.rvTrainingPlan.setAdapter(adapter);

    }

    void goTraining() {
        Intent intent = new Intent(getContext(), TrainingActivity.class);
        intent.putExtra("trainingModel", mModel.getId());
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }
}