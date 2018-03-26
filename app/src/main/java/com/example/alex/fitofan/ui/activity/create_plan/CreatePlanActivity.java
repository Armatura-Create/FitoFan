package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityCreatePlanBinding;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.CustomDialog;
import com.example.alex.fitofan.utils.db.DatabaseHelper;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class CreatePlanActivity extends AppCompatActivity implements CreatePlanContract.View {

    private ActivityCreatePlanBinding mBinding;
    private CreatePlanPresenter mPresenter;
    private RecyclerAdapterCreatePlan adapter;
    private int tempPosition;
    private ImageView imageExercise;
    private CardView cvExercise;
    private TrainingModel mModel;
    private Dao<TrainingModel, Integer> mTrainings = null;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SELECT_IMAGE_PLAN = 200;
    private static final int SELECT_IMAGE_EXERCISE = 300;
    private static final int FILE_SELECT_CODE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_plan);
        mPresenter = new CreatePlanPresenter(this);
        mModel = new TrainingModel();
        ExerciseModel exerciseModel = new ExerciseModel();
        ArrayList<ExerciseModel> exerciseModels = new ArrayList<>();
        exerciseModels.add(exerciseModel);
        mModel.setExercises(exerciseModels);
        initDB(getIntent().getIntExtra("trainingModel", -1));
        initListeners();
        initRecyclerView(getIntent().getIntExtra("trainingModel", -1));
    }

    private void initDB(int trainingModelEdit) {
        try {
            mTrainings = OpenHelperManager.getHelper(this, DatabaseHelper.class).getTrainingDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (trainingModelEdit >= 0) {
            try {
                mTrainings = OpenHelperManager.getHelper(this, DatabaseHelper.class).getTrainingDAO();
                assert mTrainings != null;
                mModel = mTrainings.queryForId(trainingModelEdit);
                initDataTraining();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initDataTraining() {
//        mBinding.contentCreatePlan.tvTotalTime.setText(FormatTime.formatTime(
//                mModel.getTime()
//        ));
//        mBinding.contentCreatePlan.etTrainingNameCreate.setText(
//                mModel.getName()
//        );
//        mBinding.contentCreatePlan.edDescription.setText(
//                mModel.getDescription()
//        );
//        if (mModel.getImage() != null) {
//            mBinding.contentCreatePlan.imageTrainingPlanCard.setVisibility(View.VISIBLE);
//            Glide.with(getContext())
//                    .load(Uri.parse(mModel.getImage()))
//                    .placeholder(R.mipmap.icon)
//                    .fitCenter()
//                    .thumbnail(0.5f)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(mBinding.contentCreatePlan.imageTrainingPlanCreate);
//        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> {

            Dialog dialog = CustomDialog.dialogSimple(this,
                    getResources().getString(R.string.exit),
                    getResources().getString(R.string.saved_question),
                    getResources().getString(R.string.yes),
                    getResources().getString(R.string.no));
            dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                setPlans(mModel);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.bt_negative).setOnClickListener(v1 -> {
                goToMyPlans();
                dialog.dismiss();
            });
        });
    }

    private void initRecyclerView(int id) {

        mBinding.contentCreatePlan.rvExerciseCreatePlan.setNestedScrollingEnabled(false);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterCreatePlan(this, mModel, id);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setAdapter(adapter);
    }

    void addItemExercise() {
        adapter.addItem();
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setVerticalScrollbarPosition(adapter.getItemCount() - 1);
    }

    void choosePictureExercise(int position, ImageView image, CardView cvImage) {
        tempPosition = position;
        imageExercise = image;
        cvExercise = cvImage;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_IMAGE_EXERCISE);
    }

    void choosePicturePlan(int position, ImageView image, CardView cvImage) {
        tempPosition = position;
        imageExercise = image;
        cvExercise = cvImage;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_IMAGE_PLAN);
    }

    void chooseAudioExercise(int position) {
        tempPosition = position;
        Intent audioPickerIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            audioPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            audioPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            audioPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            audioPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        audioPickerIntent.setType("audio/*");
        audioPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(audioPickerIntent, "Select a File to Upload"), FILE_SELECT_CODE);
    }

    public void requestMultiplePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_IMAGE_PLAN:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            Uri uriPlan = data.getData();
                            mModel.setImage(uriPlan.toString());
                            adapter.setImage(uriPlan, imageExercise, cvExercise);
                        }
                }
                break;
            case SELECT_IMAGE_EXERCISE:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            Uri uriExercise = data.getData();
                            mModel.getExercises().get(tempPosition - 1).setImage(uriExercise.toString());
                            Log.e("onActivityResult: ", new Gson().toJson(mModel));
                            adapter.setImage(uriExercise, imageExercise, cvExercise);
                        }
                }
                break;
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            Uri uriExercise = data.getData();
                            adapter.setAudio(uriExercise, tempPosition);
                        }
                }
                break;
        }
    }

    void setPlans(TrainingModel modelTraining) {
        Toast.makeText(this, getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();

        try {
            mTrainings.createOrUpdate(modelTraining);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        goToMyPlans();
    }

    private void goToMyPlans() {
        onBackPressed();
    }

    @Override
    public Context getContext() {
        return this;
    }
}