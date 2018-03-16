package com.example.alex.fitofan.ui.activity.preview_plan;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityPlanPreviewBinding;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.ui.activity.training.TrainingActivity;
import com.example.alex.fitofan.utils.FormatTime;
import com.example.alex.fitofan.utils.db.DatabaseHelper;
import com.google.gson.Gson;
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
        initText();
        initRecyclerView();
        initButton(isGoTo);
    }

    private void getPlanFromBD(int trainingModel) {
        if(trainingModel >= 0) {
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

    private void initText() {
        mBinding.content.tvTrainingName.setText(mModel.getName());
        mBinding.content.tvTotalTime.setText(FormatTime.formatTime(
                mModel.getTime()
        ));
    }

    private void initButton(int i) {
        switch (i) {
            case 1:

                break;
            case 2:
                mBinding.content.tvBottomPreviewPlan.setText(getResources().getString(R.string.start_training));
                mBinding.content.btSaveAllPlan.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), TrainingActivity.class);
                    intent.putExtra("trainingModel", mModel.getId());
                    startActivity(intent);
                });
                break;
            default:
                break;
        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initRecyclerView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            mBinding.content.rvTrainingPlan.setNestedScrollingEnabled(true);
        else
            mBinding.content.rvTrainingPlan.setNestedScrollingEnabled(false);
        mBinding.content.rvTrainingPlan.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rvTrainingPlan.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterPreviewPlan(mModel, this, isGoTo);
        mBinding.content.rvTrainingPlan.setAdapter(adapter);

    }

    @Override
    public Context getContext() {
        return this;
    }
}