package com.example.alex.fitofan.ui.activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityFullScreenImageBinding;

import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;
import static com.bumptech.glide.request.RequestOptions.priorityOf;

public class FullScreenImage extends AppCompatActivity {

    private ActivityFullScreenImageBinding binding;
    private String imageURLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen_image);
        binding.toolbar.setTitle(getIntent().getStringExtra("name"));
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        imageURLs = getIntent().getStringExtra("url");

        Glide.with(getApplicationContext()) //передаем контекст приложения
                .load(Uri.parse(imageURLs))
                .apply(fitCenterTransform())
                .apply(priorityOf(Priority.IMMEDIATE))
                .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(binding.imageViewFullScreen); //ссылка на ImageView


//        binding.webViewImage.setBackgroundColor(Color.BLACK);
//        binding.webViewImage.getSettings().setSupportZoom(true);
//        binding.webViewImage.getSettings().setBuiltInZoomControls(true);
//        binding.webViewImage.setPadding(0, 0, 0, 0);
//        binding.webViewImage.setScrollbarFadingEnabled(true);
//        binding.webViewImage.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        binding.webViewImage.loadUrl(host + imageURLs);

    }
}
