package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.crashlytics.android.Crashlytics;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityCreatePlanBinding;
import com.example.alex.fitofan.interfaces.GetExercisePhotosLoader;
import com.example.alex.fitofan.interfaces.ILoadingEdit;
import com.example.alex.fitofan.interfaces.ILoadingPlan;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.models.GetExerciseModel;
import com.example.alex.fitofan.models.GetExercisePhotos;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.CompressImage;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.CustomDialog.CustomDialog;
import com.example.alex.fitofan.utils.VideoTrim;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import xyz.belvi.intentmanip.IntentUtils.IntentAppend;
import xyz.belvi.intentmanip.IntentUtils.IntentCallBack.ResolvedIntentListener;
import xyz.belvi.intentmanip.IntentUtils.ManipUtils;
import xyz.belvi.intentmanip.IntentUtils.Models.PreparedIntent;
import xyz.belvi.intentmanip.IntentUtils.Models.ResolveIntent;
import xyz.belvi.intentmanip.LaunchIntent;

public class CreatePlanActivity extends AppCompatActivity implements ILoadingPlan, LikeStatus, GetExercisePhotosLoader, ILoadingEdit {

    private ActivityCreatePlanBinding mBinding;
    private RecyclerAdapterCreatePlan adapter;
    private ImageView imageExercise;
    private CardView cvExercise;
    private GetPlanModel mModel;
    private Dialog mProgressDialog;
    private ArrayList<String> deleteExercises = new ArrayList<>();
    private ArrayList<String> deletePhotos = new ArrayList<>();

    private static final int PERMISSION_REQUEST_CODE = 110;
    private static final int VIDEO_CAPTURE = 101;
    private static final int VIDEO_TRIM = 102;
    private static final int FILE_SELECT_CODE = 400;

    private boolean isEdit;
    private boolean isSend;
    private boolean isExercise;

    private int tempPosition;
    private int number;
    private int numberImages;

    private String plan_id;
    private ProgressBar progress;
    private TextView path;
    private String idExercise;
    private LinearLayout borderAudio;
    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_plan);

        mProgressDialog = new Dialog(this);
        mProgressDialog.setContentView(R.layout.dialog_load);

        progress = mProgressDialog.findViewById(R.id.progress_bar);
        path = mProgressDialog.findViewById(R.id.text_part);

        Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressDialog.setCancelable(false);

        mModel = new GetPlanModel();
        GetExerciseModel exerciseModel = new GetExerciseModel();
        ArrayList<GetExerciseModel> exerciseModels = new ArrayList<>();
        exerciseModels.add(exerciseModel);
        mModel.setExercises(exerciseModels);
        if (getIntent().getBooleanExtra("edit", false)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("plan_id", getIntent().getStringExtra("planId"));
            if (Connection.isNetworkAvailable(this))
                Request.getInstance().getPlan(map, this);
            isEdit = true;
        } else {
            initRecyclerView();
        }
        initListeners();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (!isSend) {
            Dialog dialog = CustomDialog.dialogSimple(this,
                    getResources().getString(R.string.exit),
                    getResources().getString(R.string.close_constructor),
                    getResources().getString(R.string.yes),
                    getResources().getString(R.string.no));
            dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                super.onBackPressed();
                dialog.dismiss();
            });

            dialog.findViewById(R.id.bt_negative).setOnClickListener(v1 ->
                    dialog.dismiss());
        } else {
            super.onBackPressed();
        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v ->
                onBackPressed());
    }

    private void initRecyclerView() {
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setNestedScrollingEnabled(false);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setHasFixedSize(false);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setItemViewCacheSize(999);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterCreatePlan(this, mModel, isEdit);
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

    void recordingVideo(int position, VideoView video) {
        requestMultiplePermissions();
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            tempPosition = position;
            this.video = video;
            Intent videoGallery = new Intent();
            videoGallery.setType("video/*");
            videoGallery.setAction(Intent.ACTION_GET_CONTENT);
            Intent videoCamera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoCamera.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
            videoCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            PreparedIntent preparedIntent = new PreparedIntent(videoGallery, R.string.select_video, R.mipmap.ic_launcher);
            List<ResolveIntent> resolveIntentList = IntentAppend.appendCustomIntent(this, videoCamera, preparedIntent);
            LaunchIntent.withButtomSheetAsList(this, resolveIntentList, getResources().getString(R.string.select_action), (ResolvedIntentListener<ResolveIntent>) resolveIntent ->
                    startActivityForResult(ManipUtils.getLaunchableIntent(resolveIntent), VIDEO_CAPTURE));
        }
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
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
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
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
                        if (!isExercise) {
                            mModel.getTraining().setImage(URLEncoder.encode(CompressImage.getBase64FromBitmap(imageBitmap), "UTF-8"));
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
            case VIDEO_CAPTURE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        MediaPlayer mp = MediaPlayer.create(this, data.getData());
                        if (mp.getDuration() > 11000) {
                            Intent intent = new Intent(CreatePlanActivity.this, VideoTrim.class);
                            intent.putExtra("data", getFilePathFromVideoURI(data.getData()));
                            startActivityForResult(intent, VIDEO_TRIM);
                        } else {
                            adapter.setVideoRealPath(getFilePathFromVideoURI(data.getData()), tempPosition, video);
                            Toast.makeText(this, "TestVideo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case VIDEO_TRIM:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        adapter.setVideoRealPath(data.getStringExtra("video_trim"), tempPosition, video);
                        Toast.makeText(this, "VIDEO_TRIM " + data.getStringExtra("video_trim"), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public String getFilePathFromVideoURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Video.Media.DATA};
            cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private RequestBody getVideo(String video) {
        if (video == null) return null;
        File file = new File(video);
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("video", file.getName(),
                        RequestBody.create(MediaType.parse("video/mp4"), file))
                .addFormDataPart("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature())
                .addFormDataPart("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid())
                .addFormDataPart("exercise_id", idExercise)
                .build();
    }

    @SuppressLint("SetTextI18n")
    private void sendEditPlan(GetPlanModel sendPlanModel) {
        if (checkEditText(sendPlanModel)) {
            return;
        }

        number = 0;
        numberImages = 0;
        mProgressDialog.show();
        try {
            HashMap<String, String> training = new HashMap<>();
            training.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            training.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            training.put("plan_id", sendPlanModel.getTraining().getId());
            training.put("plan_time", sendPlanModel.getTraining().getPlan_time());
            training.put("status", "0");
            training.put("name", sendPlanModel.getTraining().getName());
            training.put("description", sendPlanModel.getTraining().getDescription());
            training.put("inventory", sendPlanModel.getTraining().getInventory());
            training.put("plan_level", sendPlanModel.getTraining().getPlanLevel());
            if (sendPlanModel.getTraining().isEditPhoto())
                training.put("image_path", sendPlanModel.getTraining().getImage());
            if (Connection.isNetworkAvailable(this)) {
                progress.setProgress(0);
                path.setText(number + 1 + "/" + (sendPlanModel.getExercises().size() + 1));
                Request.getInstance().editPlan(training, this);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            mProgressDialog.dismiss();
            Toast.makeText(getContext(), "Сбой при редактировании", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestsEdit() {
        deleteExercises();
        deletePhotos();
        requestExerciseEdit();
    }

    private void deletePhotos() {
        for (int i = 0; i < deletePhotos.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("exercise_photo_id", deletePhotos.get(i));
            if (Connection.isNetworkAvailable(this))
                Request.getInstance().delExercisePhoto(map, this);
        }
    }

    private void deleteExercises() {
        for (int i = 0; i < deleteExercises.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("exercise_id", deleteExercises.get(i));
            if (Connection.isNetworkAvailable(this)) {
                Request.getInstance().delExercise(map, this);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    void sendPlan(GetPlanModel sendPlanModel) {
        mModel = sendPlanModel;

        if (checkEditText(sendPlanModel)) {
            return;
        }

        if (isEdit) {
            sendEditPlan(sendPlanModel);
            return;
        }

        number = 0;
        numberImages = 0;

        mProgressDialog.show();
        try {
            HashMap<String, String> training = new HashMap<>();
            training.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            training.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            training.put("plan_time", String.valueOf(sendPlanModel.getTraining().getPlan_time()));
            training.put("status", "0");
            training.put("name", sendPlanModel.getTraining().getName());
            training.put("description", sendPlanModel.getTraining().getDescription());
            training.put("inventory", sendPlanModel.getTraining().getInventory());
            training.put("image_path", sendPlanModel.getTraining().getImage());
            training.put("plan_level", sendPlanModel.getTraining().getPlanLevel());
            if (Connection.isNetworkAvailable(this)) {
                progress.setProgress(0);
                path.setText(number + 1 + "/" + (sendPlanModel.getExercises().size() + 1));
                Request.getInstance().sendPlan(training, this);
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
            mProgressDialog.dismiss();
            Toast.makeText(getContext(), getResources().getString(R.string.failed_to_save), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkEditText(GetPlanModel modelTraining) {
        if (modelTraining.getTraining().getName() == null) {
            Toast.makeText(this, getResources().getString(R.string.training)
                    + " " + getResources().getString(R.string.no_entered_name), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (modelTraining.getTraining().getDescription() == null) {
            Toast.makeText(this, getResources().getString(R.string.training)
                    + getResources().getString(R.string.no_entered_description), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (modelTraining.getTraining().getImage() == null) {
            Toast.makeText(this, getResources().getString(R.string.training)
                    + " " + getResources().getString(R.string.no_put_image), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (modelTraining.getTraining().getInventory() == null)
            modelTraining.getTraining().setInventory("");
        for (int i = 0; i < modelTraining.getExercises().size(); i++) {
            if (modelTraining.getExercises().get(i).getName() == null) {
                Toast.makeText(this, getResources().getString(R.string.exercise)
                        + " #" + (i + 1) + " " + getResources().getString(R.string.no_entered_name), Toast.LENGTH_SHORT).show();
                return true;
            }
            if (modelTraining.getExercises().get(i).getDescription() == null)
                modelTraining.getExercises().get(i).setDescription("");
            if (modelTraining.getExercises().get(i).getMusicUrls() == null)
                modelTraining.getExercises().get(i).setMusicUrls("");
            if (modelTraining.getExercises().get(i).getImage() == null)
                modelTraining.getExercises().get(i).setImage("");
        }
        return false;
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
        path.setText(number + 2 + "/" + (mModel.getExercises().size() + 1));

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
            map.put("music_urls", URLEncoder.encode(mModel.getExercises().get(number).getMusicUrls(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("image", mModel.getExercises().get(number).getPhotos().size() > 0 ?
                mModel.getExercises().get(number).getPhotos().get(0).getImagePath() :
                "");
        if (Connection.isNetworkAvailable(this))
            Request.getInstance().addExercise(map, this);
    }

    @SuppressLint("SetTextI18n")
    private void requestExerciseEdit() {

        numberImages = 0;
        HashMap<String, String> map = new HashMap<>();
        int prog = (number + 1) * 100 / (mModel.getExercises().size() + 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progress.setProgress(prog, true);
        } else {
            progress.setProgress(prog);
        }
        path.setText(number + 2 + "/" + (mModel.getExercises().size() + 1));

        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("name", mModel.getExercises().get(number).getName());
        map.put("description", mModel.getExercises().get(number).getDescription());
        map.put("id", String.valueOf(mModel.getExercises().get(number).getId()));
        map.put("plan_id", plan_id);
        map.put("recovery_time", String.valueOf(mModel.getExercises().get(number).getRecoveryTime()));
        map.put("count_repetitions", String.valueOf(mModel.getExercises().get(number).getCountRepetitions()));
        map.put("exercise_time", String.valueOf(mModel.getExercises().get(number).getTime()));
        map.put("time_between", String.valueOf(mModel.getExercises().get(number).getTimeBetween()));
        try {
            map.put("music_urls", URLEncoder.encode(mModel.getExercises().get(number).getMusicUrls(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // TODO ЧТо делать если все удалили фотки
        map.put("image", mModel.getExercises().get(number).getPhotos().size() > 0 ?
                (mModel.getExercises().get(number).getPhotos().get(0).isEdit() ? mModel.getExercises().get(number).getPhotos().get(0).getImagePath() :
                        "") : "");
        if (Connection.isNetworkAvailable(this))
            Request.getInstance().editExercises(map, this);
    }

    private void requestExerciseImagesAdd() {
        numberImages++;
        if (!isEdit) {
            if (mModel.getExercises().get(number).getPhotos().size() > numberImages) {
                HashMap<String, String> map = new HashMap<>();
                map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                map.put("exercise_id", idExercise);
                map.put("image", mModel.getExercises().get(number).getPhotos().get(numberImages).getImagePath());
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
        } else {
            if (mModel.getExercises().get(number).getPhotos().size() > numberImages) {
                if (mModel.getExercises().get(number).getPhotos().get(numberImages).isEdit()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                    map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                    map.put("exercise_id", idExercise);
                    map.put("image", mModel.getExercises().get(number).getPhotos().get(numberImages).getImagePath());
                    if (Connection.isNetworkAvailable(this))
                        Request.getInstance().addExercisePhoto(map, this);
                } else {
                    requestExerciseImagesAdd();
                }
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
    }

    private void requestExerciseVideoAdd() {
        Request.getInstance().changeExerciseVideo(getVideo(mModel.getExercises().get(number).getVideoUrl()), this);
    }

    @Override
    public void onSuccess(String info) {
        idExercise = info;
        if (!isEdit) {
            if (mModel.getExercises().get(number).getVideoUrl() != null && !mModel.getExercises().get(number).getVideoUrl().equals("")) {
                requestExerciseVideoAdd();
            } else if (mModel.getExercises().get(number).getPhotos() != null && mModel.getExercises().get(number).getPhotos().size() > 1) {
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
        } else {
            if (mModel.getExercises().get(number).getVideoUrl() != null && !mModel.getExercises().get(number).getVideoUrl().equals("")) {
                requestExerciseVideoAdd();
            } else if (mModel.getExercises().get(number).getPhotos() != null && mModel.getExercises().get(number).getPhotos().size() > 1) {
                requestExerciseImagesAdd();
            } else if (mModel.getExercises().size() > number + 1) {
                number++;
                if (mModel.getExercises().get(number).isNew())
                    requestExerciseAdd();
                else
                    requestExerciseEdit();
            } else {
                mProgressDialog.cancel();
                Toast.makeText(getContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                isSend = true;
                onBackPressed();
            }
        }

    }

    @Override
    public void onSuccess(GetPlanModel info, String request) {
        switch (request) {
            case "sendPlan":
                plan_id = info.getTraining().getId();
                number = 0;
                if (mModel.getExercises().size() > 0) {
                    requestExerciseAdd();
                } else {
                    mProgressDialog.cancel();
                }
                break;
            case "editPlan":
                plan_id = info.getTraining().getId();
                number = 0;
                if (mModel.getExercises().size() > 0) {
                    requestsEdit();
                } else {
                    mProgressDialog.cancel();
                }
                break;
            case "getPlan":
                if (info != null) {
                    mModel = info;
                    isEdit = true;
                    initRecyclerView();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String info, String request) {
        idExercise = info;
        if (request.equals("editExercise"))
            if (mModel.getExercises().get(number).getPhotos() != null && mModel.getExercises().get(number).getPhotos().size() > 1) {
                requestExerciseImagesAdd();
            } else if (mModel.getExercises().size() > number + 1) {
                number++;
                if (mModel.getExercises().get(number).isNew())
                    requestExerciseAdd();
                else
                    requestExerciseEdit();
            } else {
                mProgressDialog.cancel();
                Toast.makeText(getContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                isSend = true;
                onBackPressed();
            }
    }

    @Override
    public void onSuccess(GetExercisePhotos info, String request) {
        if (request.equals("addExercisePhoto"))
            requestExerciseImagesAdd();
        else if (request.equals("addVideo")) {
            if (mModel.getExercises().size() > number + 1) {
                number++;
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
    public void onFailure(String message) {
        mProgressDialog.cancel();
        Toast.makeText(getContext(), getResources().getString(R.string.failed_to_save), Toast.LENGTH_SHORT).show();
    }

    public ArrayList<String> getDeleteExercises() {
        return deleteExercises;
    }

    public ArrayList<String> getDeletePhotos() {
        return deletePhotos;
    }

    public void setDeletePhotos(ArrayList<String> deletePhotos) {
        this.deletePhotos = deletePhotos;
    }
}