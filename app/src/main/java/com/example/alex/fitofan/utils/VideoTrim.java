package com.example.alex.fitofan.utils;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityVideoTrimBinding;

import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class VideoTrim extends AppCompatActivity implements OnTrimVideoListener {

    private ActivityVideoTrimBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_trim);
        mBinding.timeLine.setDestinationPath("/storage/emulated/0/DCIM/FitoFan/");
        mBinding.timeLine.setVideoURI(Uri.parse(getIntent().getStringExtra("data")));
        mBinding.timeLine.setMaxDuration(10);
        mBinding.timeLine.setOnTrimVideoListener(this);
    }

    @Override
    public void getResult(final Uri uri) {
        Intent intent = new Intent();
        intent.putExtra("video_trim", uri.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void cancelAction() {
        onBackPressed();
    }
}
