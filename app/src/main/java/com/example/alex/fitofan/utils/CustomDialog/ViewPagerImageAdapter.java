package com.example.alex.fitofan.utils.CustomDialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.models.PhotoModel;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class ViewPagerImageAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<PhotoModel> imageUrls;

    ViewPagerImageAdapter(Context context, ArrayList<PhotoModel> images) {
        this.context = context;
        this.imageUrls = images;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);

        Glide.with(context)
                .load(Uri.parse(imageUrls.get(position).getImagePath()))
                .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .thumbnail(0.1f)
                .transition(withCrossFade())
                .into(imageView);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
