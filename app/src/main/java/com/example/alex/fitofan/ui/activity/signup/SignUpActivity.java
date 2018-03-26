package com.example.alex.fitofan.ui.activity.signup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivitySignUpBinding;
import com.example.alex.fitofan.ui.activity.main.MainActivity;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    //TODO: It might be useful to use DataBinding
    //TODO: Будет хорошо, если вы будете использовать DataBinding
    private ActivitySignUpBinding mBinding;
    private SignUpPresenter presenter;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SELECT_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        presenter = new SignUpPresenter(this);
        initListeners();
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mBinding.content.userPhoto.setOnClickListener(v -> {
            requestMultiplePermissions();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                chooseAvatar();
        });
        mBinding.content.btRegister.setOnClickListener(v -> presenter.register("1", "1", "1","1"));
    }

    void chooseAvatar() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_IMAGE);
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
            case SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            setUserImage(data.getData());
                        }
                }
                break;
        }
    }

    void setUserImage(Uri data) {
        Glide.with(getContext())
                .load(data)
                .into(mBinding.content.userPhoto);
    }

    @Override
    public void goToSingIn() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}