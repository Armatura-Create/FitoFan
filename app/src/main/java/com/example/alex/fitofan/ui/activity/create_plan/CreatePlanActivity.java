package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityCreatePlanBinding;
import com.example.alex.fitofan.models.UriImage;
import com.example.alex.fitofan.utils.CustomDialog;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreatePlanActivity extends AppCompatActivity implements CreatePlanContract.View {

    private ActivityCreatePlanBinding mBinding;
    private CreatePlanPresenter mPresenter;
    private Dialog mDialog;
    private Uri imageUri;
    private RecyclerAdapterCreatePlan adapter;
    private ArrayList<UriImage> mUriImages;
    private int imagePosition;
    private ImageView imageExercise;
    private CardView cvExercise;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_ACTIVITY = 200;
    private static final int REQUEST_CODE_DIALOG = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_plan);
        mPresenter = new CreatePlanPresenter(this);
        mUriImages = new ArrayList<>();
        initListeners();
        initRecyclerView();
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        mBinding.contentCreatePlan.etTrainingNameCreate.setOnClickListener(v -> {
            mDialog = CustomDialog.dialog(this, getResources().getString(R.string.training_class),
                    getResources().getString(R.string.class_description), getResources().getString(R.string.save));
            mDialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                EditText et = mDialog.findViewById(R.id.et_add_field_dialog);
                mBinding.contentCreatePlan.etTrainingNameCreate.setText(et.getText());
                mDialog.dismiss();
            });
        });

        mBinding.contentCreatePlan.edDescription.setOnClickListener(v -> {
            mDialog = CustomDialog.dialog(this, getResources().getString(R.string.training_description),
                    getResources().getString(R.string.description_description), getResources().getString(R.string.save));
            mDialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                EditText et = mDialog.findViewById(R.id.et_add_field_dialog);
                mBinding.contentCreatePlan.edDescription.setText(et.getText());
                mDialog.dismiss();
            });
        });

        //add image
        mBinding.contentCreatePlan.btAddMediafile.setOnClickListener(v -> {requestMultiplePermissions();
            if (ContextCompat.checkSelfPermission(CreatePlanActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                choosePicturePlan();
        });

        //add item exercise
        mBinding.contentCreatePlan.btAddItemExercise.setOnClickListener(v -> addItemExercise());
    }

    private void initRecyclerView() {

        mBinding.contentCreatePlan.rvExerciseCreatePlan.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterCreatePlan(1, this);
        mBinding.contentCreatePlan.rvExerciseCreatePlan.setAdapter(adapter);

    }

    void delItemExercise(int position){
        adapter.setСount(adapter.getСount() - 1);
        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();
    }

    void addItemExercise(){
        adapter.setСount(adapter.getСount() + 1);
        adapter.notifyItemInserted(adapter.getСount());
        adapter.notifyDataSetChanged();
    }

    void addImageExercise(int position, ImageView image, CardView cvImage){
        imagePosition = position;
        imageExercise = image;
        cvExercise = cvImage;
        choosePictureExercise();

    }

    private void choosePictureExercise() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_DIALOG);
    }

    private void choosePicturePlan() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_ACTIVITY);
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
        if (requestCode == REQUEST_CODE_ACTIVITY) {
            if (data != null)
                if (data.getData() != null) {
                    mBinding.contentCreatePlan.imageTrainingPlanCard.setVisibility(View.VISIBLE);
                    imageUri = data.getData();
                    Glide.with(getContext()).load(imageUri).into(mBinding.contentCreatePlan.imageTrainingPlanCreate);
                }
        }
        if (requestCode == REQUEST_CODE_DIALOG){
            if (data != null)
                if (data.getData() != null) {
                    Uri uriExercise = data.getData();
                    UriImage uriImage = new UriImage();
                    uriImage.setUri(uriExercise);
                    mUriImages.add(imagePosition, uriImage);
                    adapter.setImage(uriExercise, imageExercise, cvExercise);
                }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}