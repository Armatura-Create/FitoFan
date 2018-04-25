package com.example.alex.fitofan.ui.activity.preview_plan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityPlanPreviewBinding;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.ui.activity.settings.SettingActivity;
import com.example.alex.fitofan.ui.activity.training.TrainingActivity;
import com.example.alex.fitofan.utils.CustomDialog;
import com.example.alex.fitofan.utils.FormatTime;
import com.example.alex.fitofan.utils.ItemClickSupport;
import com.example.alex.fitofan.utils.UnpackingTraining;
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
        setSupportActionBar(mBinding.toolbar);
        isGoTo = getIntent().getIntExtra("isGoTo", 0);
        getPlanFromBD(getIntent().getIntExtra("trainingModel", -1));
        initListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preview_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(getIntent().getIntExtra("trainingModel", -1) != -1) {
            if (id == R.id.action_edit) {
                Intent intent = new Intent(getContext(), CreatePlanActivity.class);
                intent.putExtra("trainingModel", getIntent().getIntExtra("trainingModel", -1));
                startActivity(intent);
                finish();
                return true;
            }
            if (id == R.id.action_remove) {
                Dialog dialog = CustomDialog.dialogSimple(getContext(),
                        getResources().getString(R.string.remove),
                        null,
                        getResources().getString(R.string.yes),
                        getResources().getString(R.string.no));
                dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                    Toast.makeText(getContext(), getResources().getString(R.string.removed), Toast.LENGTH_SHORT).show();
                    deletePlan(getIntent().getIntExtra("trainingModel", -1));
                    onBackPressed();
                    dialog.dismiss();
                });

                dialog.findViewById(R.id.bt_negative).setOnClickListener(v1 -> {
                    dialog.dismiss();
                });
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void deletePlan(int position) {
        try {
            mTrainings.deleteById(position);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            mBinding.content.tvTrainingName.setText(mModel.getName());
            initRecyclerView();
        } else {
            mModel = new TrainingModel();
        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mBinding.content.goToTraining.setOnClickListener(v -> {
            goTraining();
        });

        ItemClickSupport.addTo(mBinding.content.rv).setOnItemClickListener((recyclerView, position, v) -> {
            if(!adapter.getModel().get(position).isRest()){
                Dialog dialog = CustomDialog.card(getContext(),
                        adapter.getModel().get(position).getName(),
                        adapter.getModel().get(position).getDescription(),
                       adapter.getModel().get(position).getImage());
                dialog.findViewById(R.id.cancel_dialog).setOnClickListener(v1 -> {
                    dialog.dismiss();
                });
            }
        });
    }

    private void initRecyclerView() {

        mBinding.content.rv.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterPreviewPlan(this, UnpackingTraining.buildExercises(mModel));
        mBinding.content.rv.setAdapter(adapter);

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