package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityCreatePlanBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.SendExerciseModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.CompressImage;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.CustomDialog.CustomDialog;
import com.example.alex.fitofan.utils.db.DatabaseHelper;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class CreatePlanActivity extends AppCompatActivity implements ILoadingStatus<GetPlanModel>, LikeStatus {

    private ActivityCreatePlanBinding mBinding;
    private RecyclerAdapterCreatePlan adapter;
    private int tempPosition;
    private ImageView imageExercise;
    private CardView cvExercise;
    private TrainingModel mModel;
    private ProgressDialog mProgressDialog;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SELECT_IMAGE_PLAN = 200;
    private static final int SELECT_IMAGE_EXERCISE = 300;
    private static final int FILE_SELECT_CODE = 400;
    private boolean isEdit;
    private boolean isSend;
    private LinearLayout borderAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_plan);
        mProgressDialog = new ProgressDialog(this);
        mModel = new TrainingModel();
        ExerciseModel exerciseModel = new ExerciseModel();
        ArrayList<ExerciseModel> exerciseModels = new ArrayList<>();
        exerciseModels.add(exerciseModel);
        mModel.setExercises(exerciseModels);
        if (getIntent().getBooleanExtra("edit", false)) {
            initEdit();
            isEdit = true;
        }
        initListeners();
        initRecyclerView(getIntent().getIntExtra("trainingModel", -1));
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }

        return super.dispatchTouchEvent(event);
    }

    private void initEdit() {
        if (Connection.isNetworkAvailable(this)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("plan_id", getIntent().getStringExtra("planId"));
            Request.getInstance().getPlan(map, this);
        }

    }

    @Override
    public void onBackPressed() {
        if(!isSend) {
            Dialog dialog = CustomDialog.dialogSimple(this,
                    getResources().getString(R.string.exit),
                    "",
                    getResources().getString(R.string.yes),
                    getResources().getString(R.string.no));
            dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                super.onBackPressed();
                dialog.dismiss();
            });

            dialog.findViewById(R.id.bt_negative).setOnClickListener(v1 -> {
                dialog.dismiss();
            });
        } else {
            super.onBackPressed();
        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initRecyclerView(int id) {
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setNestedScrollingEnabled(false);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setHasFixedSize(false);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setItemViewCacheSize(999);
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

    void chooseAudioExercise(int position, LinearLayout borderAudio) {
        tempPosition = position;
        this.borderAudio = borderAudio;
        Intent audioPickerIntent;
        audioPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        audioPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        audioPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
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
                            Bitmap imageBitmap = null;
                            try {
                                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPlan);
                                imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
                                mModel.setImage(URLEncoder.encode(CompressImage.getBase64FromBitmap(imageBitmap), "UTF-8"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            adapter.setImage(uriPlan, imageExercise, cvExercise);
                        }
                }
                break;
            case SELECT_IMAGE_EXERCISE:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            Uri uriExercise = data.getData();
                            Bitmap imageBitmap = null;
                            try {
                                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriExercise);
                                imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
                                mModel.getExercises().get(tempPosition - 1).setImage(CompressImage.getBase64FromBitmap(imageBitmap));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            adapter.setImage(uriExercise, imageExercise, cvExercise);
                        }
                }
                break;
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            Uri uriExercise = data.getData();
                            adapter.setAudio(uriExercise, tempPosition, borderAudio);
                        }
                }
                break;
        }
    }

    void sendPlan(TrainingModel modelTraining) {

        if (!checkEditText(modelTraining)) {
            Toast.makeText(this, "Введены не все данные!", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        if (modelTraining.getImage() != null) {
            try {
                requestMultiplePermissions();
                ArrayList<SendExerciseModel> exercise = new ArrayList<>();
                HashMap<String, String> training = new HashMap<>();
                training.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                training.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                training.put("plan_time", String.valueOf(modelTraining.getTime()));
                training.put("status", "0");
                training.put("name", modelTraining.getName());
                training.put("description", modelTraining.getDescription());
                training.put("image_path", modelTraining.getImage());
                for (int i = 0; i < modelTraining.getExercises().size(); i++) {
                    SendExerciseModel model = new SendExerciseModel();
                    model.setCountRepetitions(modelTraining.getExercises().get(i).getCountRepetitions());
                    model.setDescription(modelTraining.getExercises().get(i).getDescription());
                    model.setName(modelTraining.getExercises().get(i).getName());
                    model.setRecoveryTime(modelTraining.getExercises().get(i).getRecoveryTime());
                    model.setTime(modelTraining.getExercises().get(i).getTime());
                    model.setTimeBetween(modelTraining.getExercises().get(i).getTimeBetween());
                    if (modelTraining.getExercises().get(i).getAudio() != null)
                        model.setMusicUrls(modelTraining.getExercises().get(i).getAudio());
                    if (modelTraining.getExercises().get(i).getImage() != null) {
                        model.setImagePath(modelTraining.getExercises().get(i).getImage());
                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(this, "Add exercise images", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    exercise.add(model);
                    training.put("exercises", URLEncoder.encode(new Gson().toJson(exercise), "UTF-8"));
                }
                if (!isEdit)
                    Request.getInstance().sendPlan(training, this);
                else
                    Request.getInstance().editPlan(training, this);
            } catch (Exception e) {
                e.printStackTrace();
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), "Сбой при сохранении", Toast.LENGTH_SHORT).show();
            }
        } else {
            mProgressDialog.dismiss();
            Toast.makeText(this, "Add training image", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkEditText(TrainingModel modelTraining) {
        if (modelTraining.getName() == null)
            return false;
        if (modelTraining.getDescription() == null)
            return false;
        for (int i = 0; i < modelTraining.getExercises().size(); i++) {
            if (modelTraining.getExercises().get(i).getName() == null)
                return false;
            if (modelTraining.getExercises().get(i).getDescription() == null)
                return false;
        }
        return true;
    }

    public Context getContext() {
        return this;
    }

    @Override
    public void onSuccess(String info) {
        mProgressDialog.dismiss();
        if (info.equals("edit"))
            Toast.makeText(getContext(), getResources().getString(R.string.edited), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
        isSend = true;
        onBackPressed();
    }

    @Override
    public void onSuccess(GetPlanModel info) {

    }

    @Override
    public void onFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getContext(), "Сбой при сохранении", Toast.LENGTH_SHORT).show();
    }
}