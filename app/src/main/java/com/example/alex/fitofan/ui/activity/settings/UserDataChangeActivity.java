package com.example.alex.fitofan.ui.activity.settings;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.drm.ProcessedData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityChangeUserDataBinding;
import com.example.alex.fitofan.interfaces.GetMyData;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.CheckerInputData;
import com.example.alex.fitofan.utils.CompressImage;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

import okio.Utf8;

import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class UserDataChangeActivity extends AppCompatActivity implements ILoadingStatus<String>, GetMyData {

    private ActivityChangeUserDataBinding mBinding;

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int SELECT_IMAGE = 300;
    private Menu menu;
    private boolean changePhoto;
    private Uri uri;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_data);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_user_data);
        setSupportActionBar(mBinding.toolbar);

        initAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_change, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            if (CheckerInputData.isEmail(mBinding.email.getText().toString().trim())) {
                HashMap<String, String> map = new HashMap<>();
                map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                map.put("name", mBinding.name.getText().toString().trim());
                map.put("surname", mBinding.surname.getText().toString().trim());
                map.put("location", mBinding.location.getText().toString().trim());
                map.put("mystory", mBinding.myStory.getText().toString().trim());
                map.put("email", mBinding.email.getText().toString().trim());
                if (Connection.isNetworkAvailable(this)) {
                    Request.getInstance().changeUserInfo(map, this);
                    Request.getInstance().changeUserEmail(map, this);
                }
            }


            if (changePhoto) {
                try {
                    mProgressDialog.show();
                    Bitmap imageBitmap = null;
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    assert imageBitmap != null;
                    imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                    map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                    map.put("image", URLEncoder.encode(CompressImage.getBase64FromBitmap(imageBitmap), "UTF-8"));
                    if (Connection.isNetworkAvailable(this))
                        Request.getInstance().changeUserPhoto(map, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    void choosePicture() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_IMAGE);
    }

    private void initAll() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        mBinding.tvChange.setOnClickListener(v -> {
            requestMultiplePermissions();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                choosePicture();
        });

        if (MSharedPreferences.getInstance().getUserInfo() != null &&
                new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class) != null
                && new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser() != null) {
            Glide.with(this)
                    .load(Uri.parse(new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getImage_url()))
                    .into(mBinding.imageUser);

            mBinding.name.setText(new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getName());
            mBinding.surname.setText(new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSurname());
            mBinding.location.setText(new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getLocation());
            mBinding.myStory.setText(new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getMystory());
            mBinding.email.setText(new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getEmail());
        }
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
                            uri = data.getData();
                            Glide.with(this)
                                    .load(uri)
                                    .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                                    .into(mBinding.imageUser);
                            changePhoto = true;
                        }
                }
                break;
        }
    }

    @Override
    public void onSuccess(String info) {
        if (info.equals("truePhoto") || info.equals("trueData")) {
            if (Connection.isNetworkAvailable(this))
                Request.getInstance().getMyData(this);
        }
        if (info.equals("truePhoto"))
            mProgressDialog.dismiss();
        if (info.equals("trueData"))
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(User info) {
        GetUserModel app = new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class);
        app.getUser().setLocation(info.getLocation());
        app.getUser().setName(info.getName());
        app.getUser().setSurname(info.getSurname());
        app.getUser().setMystory(info.getMystory());

        MSharedPreferences.getInstance().setUserInfo(new Gson().toJson(app));
    }

    @Override
    public void onFailure(String message) {

    }
}
