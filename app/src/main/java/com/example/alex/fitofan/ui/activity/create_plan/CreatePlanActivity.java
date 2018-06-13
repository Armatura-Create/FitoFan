package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityCreatePlanBinding;
import com.example.alex.fitofan.interfaces.GetExercisePhotosLoader;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.interfaces.SendPlan;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.GetExercisePhotos;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.CompressImage;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.CustomDialog.CustomDialog;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CreatePlanActivity extends AppCompatActivity implements ILoadingStatus<GetPlanModel>, LikeStatus, SendPlan, GetExercisePhotosLoader {

    private ActivityCreatePlanBinding mBinding;
    private RecyclerAdapterCreatePlan adapter;
    private int tempPosition;
    private ImageView imageExercise;
    private CardView cvExercise;
    private TrainingModel mModel;
    private Dialog mProgressDialog;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int FILE_SELECT_CODE = 400;
    private boolean isEdit;
    private boolean isSend;
    private LinearLayout borderAudio;
    private int number;
    private String plan_id;
    private boolean isExercise;
    private ProgressBar progress;
    private TextView parth;
    private boolean isManyImages;
    private String idExercise;
    private int numberImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_plan);

        mProgressDialog = new Dialog(this);
        mProgressDialog.setContentView(R.layout.dialog_load);

        progress = mProgressDialog.findViewById(R.id.progress_bar);
        parth = mProgressDialog.findViewById(R.id.text_part);

        Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressDialog.setCancelable(false);

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
        if (event.getAction() == MotionEvent.ACTION_UP) {
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
        if (!isSend) {
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

    void choosePicture(int position, ImageView image, CardView cvImage) {
        tempPosition = position;
        imageExercise = image;
        cvExercise = cvImage;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        this.isExercise = false;
    }

    public void choosePicture(int position) {
        tempPosition = position;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
        isExercise = true;
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
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    Bitmap imageBitmap = null;
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
                        if (!isExercise) {
                            mModel.setImage(URLEncoder.encode(CompressImage.getBase64FromBitmap(imageBitmap), "UTF-8"));
                            adapter.setImage(resultUri, imageExercise, cvExercise);
                        } else {
                            adapter.setImageExercise(resultUri, tempPosition);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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

    void editPlan(TrainingModel modelTraining) {

    }

    @SuppressLint("SetTextI18n")
    void sendPlan(TrainingModel modelTraining) {
        mModel = modelTraining;

        if (!checkEditText(modelTraining)) {
            Toast.makeText(this, "Введены не все данные!", Toast.LENGTH_SHORT).show();
            return;
        }

        number = 0;
        numberImages = 0;

        mProgressDialog.show();
        try {
            HashMap<String, String> training = new HashMap<>();
            training.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            training.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            training.put("plan_time", String.valueOf(modelTraining.getTime()));
            training.put("status", "0");
            training.put("name", modelTraining.getName());
            training.put("description", modelTraining.getDescription());
            training.put("inventory", modelTraining.getInvetory());
            training.put("image_path", modelTraining.getImage());
            if (Connection.isNetworkAvailable(this)) {
                progress.setProgress(0);
                parth.setText(number + 1 + "/" + (modelTraining.getExercises().size() + 1));
                Request.getInstance().sendPlan(training, this);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            mProgressDialog.dismiss();
            Toast.makeText(getContext(), "Сбой при сохранении", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkEditText(TrainingModel modelTraining) {
        if (modelTraining.getName() == null)
            return false;
        if (modelTraining.getDescription() == null)
            return false;
        if (modelTraining.getImage() == null)
            return false;
        if (modelTraining.getInvetory() == null)
            modelTraining.setInvetory("");
        for (int i = 0; i < modelTraining.getExercises().size(); i++) {
            if (modelTraining.getExercises().get(i).getName() == null)
                return false;
            if (modelTraining.getExercises().get(i).getDescription() == null)
                modelTraining.getExercises().get(i).setDescription("");
            if (modelTraining.getExercises().get(i).getAudio() == null)
                modelTraining.getExercises().get(i).setAudio("");
            if (modelTraining.getExercises().get(i).getImage() == null)
                modelTraining.getExercises().get(i).setImage("");
        }
        return true;
    }

    public Context getContext() {
        return this;
    }

    @SuppressLint("SetTextI18n")
    private void requestExerciseAdd() {
        numberImages = 0;
        HashMap<String, String> map = new HashMap<>();
        int prog = (number + 1) * 100 / (mModel.getExercises().size() + 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progress.setProgress(prog, true);
        } else {
            progress.setProgress(prog);
        }
        parth.setText(number + 2 + "/" + (mModel.getExercises().size() + 1));

        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("is_new", "1");
        map.put("name", mModel.getExercises().get(number).getName());
        map.put("description", mModel.getExercises().get(number).getDescription());

        map.put("plan_id", plan_id);
        map.put("recovery_time", String.valueOf(mModel.getExercises().get(number).getRecoveryTime()));
        map.put("count_repetitions", String.valueOf(mModel.getExercises().get(number).getCountRepetitions()));
        map.put("exercise_time", String.valueOf(mModel.getExercises().get(number).getTime()));
        map.put("time_between", String.valueOf(mModel.getExercises().get(number).getTimeBetween()));
        try {
            map.put("music_urls", URLEncoder.encode(mModel.getExercises().get(number).getAudio(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        map.put("image", mModel.getExercises().get(number).getImages().get(0));
        if (Connection.isNetworkAvailable(this))
            Request.getInstance().addExercise(map, this);
    }

    private void requestExerciseImagesAdd() {
        numberImages++;
        if (mModel.getExercises().get(number).getImages().size() > numberImages) {
            HashMap<String, String> map = new HashMap<>();
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("exercise_id", idExercise);
            map.put("image", mModel.getExercises().get(number).getImages().get(numberImages));
            if (Connection.isNetworkAvailable(this))
                Request.getInstance().addExercisePhoto(map, this);
        } else {
            number++;
            if (mModel.getExercises().size() > number) {
                requestExerciseAdd();
            } else {
                mProgressDialog.cancel();
                Toast.makeText(getContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                isSend = true;
                onBackPressed();
            }
        }

    }

    @Override
    public void onSuccess(String info) {
        idExercise = info;
        if (mModel.getExercises().get(number).getImages().size() > 1) {
            requestExerciseImagesAdd();
        } else if (mModel.getExercises().size() > number + 1) {
            number++;
            requestExerciseAdd();
        } else {
            mProgressDialog.cancel();
            Toast.makeText(getContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            isSend = true;
            onBackPressed();
        }
//        if (info.equals("edit"))
//            Toast.makeText(getContext(), getResources().getString(R.string.edited), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(GetPlanModel info) {
        if (!isEdit) {
            plan_id = info.getTraining().getId();
            number = 0;
            if (mModel.getExercises().size() > 0) {
                requestExerciseAdd();
            } else {
                mProgressDialog.cancel();
            }
        } else {
            plan_id = info.getTraining().getId();
            number = 0;
            //TODO Редатирование
        }
    }

    @Override
    public void onSuccess(GetExercisePhotos info) {
        requestExerciseImagesAdd();
    }

    @Override
    public void onFailure(String message) {
        mProgressDialog.cancel();
        Crashlytics.log(message);
        Log.e("onFailure: ", message);
        Toast.makeText(getContext(), "Сбой при сохранении", Toast.LENGTH_SHORT).show();
    }
}