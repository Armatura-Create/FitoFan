package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityCreatePlanBinding;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.CustomDialog;
import com.example.alex.fitofan.utils.FormatTime;
import com.example.alex.fitofan.utils.db.DatabaseHelper;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreatePlanActivity extends AppCompatActivity implements CreatePlanContract.View {

    private ActivityCreatePlanBinding mBinding;
    private CreatePlanPresenter mPresenter;
    private Dialog mDialog;
    private RecyclerAdapterCreatePlan adapter;
    private int tempPosition;
    private ImageView imageExercise;
    private CardView cvExercise;
    private ArrayList<ExerciseModel> mExerciseModels;
    private TrainingModel mModel;
    private ExerciseModel mExerciseModel;
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
        mExerciseModel = new ExerciseModel();
        mExerciseModels = new ArrayList<>();
        mExerciseModels.add(mExerciseModel);
        mModel.setExercises(mExerciseModels);
        initDB(getIntent().getIntExtra("trainingModel", -1));
        initListeners();
        initRecyclerView(getIntent().getIntExtra("trainingModel", -1));
        setAllTime();
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
        mBinding.contentCreatePlan.tvTotalTime.setText(FormatTime.formatTime(
                mModel.getTime()
        ));
        mBinding.contentCreatePlan.etTrainingNameCreate.setText(
                mModel.getName()
        );
        mBinding.contentCreatePlan.edDescription.setText(
                mModel.getDescription()
        );
        if (mModel.getImage() != null) {
            mBinding.contentCreatePlan.imageTrainingPlanCard.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(Uri.parse(mModel.getImage()))
                    .placeholder(R.mipmap.icon)
                    .fitCenter()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mBinding.contentCreatePlan.imageTrainingPlanCreate);
        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> {

            Dialog dialog = CustomDialog.dialogSimple(this,
                    getResources().getString(R.string.exit),
                    "Are you sure?",
                    "Yes",
                    "No");
            dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                setPlans(mModel);
                dialog.dismiss();
            });
        });

        mBinding.contentCreatePlan.etTrainingNameCreate.setOnClickListener(v -> {
            mDialog = CustomDialog.dialog(this, getResources().getString(R.string.training_class),
                    getResources().getString(R.string.class_description), getResources().getString(R.string.save), 1);
            mDialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                EditText et = mDialog.findViewById(R.id.et_add_field_dialog);
                mBinding.contentCreatePlan.etTrainingNameCreate.setText(et.getText());
                mModel.setName(et.getText().toString().trim());
                mDialog.dismiss();
            });
        });

        mBinding.contentCreatePlan.edDescription.setOnClickListener(v -> {
            mDialog = CustomDialog.dialog(this, getResources().getString(R.string.training_description),
                    getResources().getString(R.string.description_description_plan), getResources().getString(R.string.save), 1);
            mDialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                EditText et = mDialog.findViewById(R.id.et_add_field_dialog);
                mBinding.contentCreatePlan.edDescription.setText(et.getText());
                mModel.setDescription(et.getText().toString().trim());
                mDialog.dismiss();
            });
        });

        mBinding.contentCreatePlan.btSaveAllPlan.setOnClickListener(v -> {
            setPlans(mModel);
        });

        //add image
        mBinding.contentCreatePlan.btAddMediafile.setOnClickListener(v -> {
            requestMultiplePermissions();
            if (ContextCompat.checkSelfPermission(CreatePlanActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                choosePicturePlan();
        });

        //add item exercise
        mBinding.contentCreatePlan.btAddItemExercise.setOnClickListener(v -> addItemExercise());
    }

    private void initRecyclerView(int id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            mBinding.contentCreatePlan.rvExerciseCreatePlan.setNestedScrollingEnabled(true);
        else
            mBinding.contentCreatePlan.rvExerciseCreatePlan.setNestedScrollingEnabled(false);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterCreatePlan(this, mModel, id);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setAdapter(adapter);

    }

    void delItemExercise(int position) {
        adapter.delItem(position);
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

    private void choosePicturePlan() {
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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    private MultipartBody.Part getImage(Uri uri) {
        if (uri == null) return null;
        File file = new File(getRealPathFromURI(this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("company", file.getName(), requestFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_IMAGE_PLAN:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            mBinding.contentCreatePlan.imageTrainingPlanCard.setVisibility(View.VISIBLE);
                            Uri uriPlan = data.getData();

                            mModel.setImage(uriPlan.toString());
                            Glide.with(getContext()).load(uriPlan).into(mBinding.contentCreatePlan.imageTrainingPlanCreate);
                        }
                }
                break;
            case SELECT_IMAGE_EXERCISE:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            Uri uriExercise = data.getData();
                            adapter.setImage(uriExercise, imageExercise, cvExercise, tempPosition);
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
        mModel.setExercises(adapter.getExerciseModels());
        Toast.makeText(this, "Action Save", Toast.LENGTH_SHORT).show();
        Log.e("setPlans: ", new Gson().toJson(modelTraining));

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

    void setAllTime() {
        long allTime = 0L;

        for (int i = 0; i < adapter.getExerciseModels().size(); i++) {
            allTime += adapter.getExerciseModels().get(i).getTime();
//            Log.e("setAllTime", String.valueOf(adapter.getExerciseModels().get(i).getTime()));
            allTime += adapter.getExerciseModels().get(i).getTimeBetween();
//            Log.e("setAllTimeBetween: ", String.valueOf(adapter.getExerciseModels().get(i).getTimeBetween()));
        }
//        Log.e("setAllTime: ", String.valueOf(allTime));

        mModel.setTime(allTime);
        mBinding.contentCreatePlan.tvTotalTime.setText(FormatTime.formatTime(allTime));
    }

    @Override
    public Context getContext() {
        return this;
    }
}