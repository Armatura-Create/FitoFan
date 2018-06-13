package com.example.alex.fitofan.ui.activity.create_plan;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class RecyclerGridAdapterPhotos extends RecyclerView.Adapter<RecyclerGridAdapterPhotos.CardSet> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private CreatePlanActivity mActivity;
    private ArrayList<Uri> images;
    private ArrayList<String> imagesString;

    RecyclerGridAdapterPhotos(CreatePlanActivity mActivity) {
        this.mActivity = mActivity;
        images = new ArrayList<>();
        imagesString = new ArrayList<>();
    }

    void addImage(Uri uri, String string) {
        images.add(uri);
        imagesString.add(string);
        this.notifyItemInserted(images.size() - 1);
    }

    public ArrayList<String> getImagesString() {
        return imagesString;
    }

    @NonNull
    @Override
    public CardSet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Создание нового представления
        CoordinatorLayout linear = (CoordinatorLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_images_exercise, parent, false);
        return new CardSet(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull CardSet holder, final int position) {
        //Заполнение заданного представления данными

        if (images.size() > 0)
            Glide.with(mActivity.getContext())
                    .load(images.get(position))
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                    .transition(withCrossFade())
                    .into(holder.imageView);
        holder.delImage.setOnClickListener(v -> holder.removeImage(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class CardSet extends RecyclerView.ViewHolder {

        ImageButton delImage;
        ImageView imageView;

        CardSet(View v) {
            super(v);
            delImage = v.findViewById(R.id.cancel_image);
            imageView = v.findViewById(R.id.image_exercise);
        }

        void removeImage(int position) {
            images.remove(position);
            imagesString.remove(position);

            notifyItemRemoved(position);
            notifyItemRangeChanged(getAdapterPosition(), images.size());
        }
    }
}