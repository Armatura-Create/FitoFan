package com.example.alex.fitofan.ui.activity.create_plan;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.PhotoModel;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class RecyclerGridAdapterPhotos extends RecyclerView.Adapter<RecyclerGridAdapterPhotos.CardSet> {

    private boolean isEdit;
    private CreatePlanActivity mActivity;
    private ArrayList<Uri> images;
    private ArrayList<PhotoModel> imagesString;
    private RecyclerAdapterCreatePlan adapterMain;

    RecyclerGridAdapterPhotos(CreatePlanActivity mActivity) {
        this.mActivity = mActivity;
        images = new ArrayList<>();
        imagesString = new ArrayList<>();
    }

    RecyclerGridAdapterPhotos(CreatePlanActivity mActivity, String image, ArrayList<PhotoModel> imagesString, RecyclerAdapterCreatePlan adapterMain) {
        this.mActivity = mActivity;
        this.imagesString = new ArrayList<>();
        this.adapterMain = adapterMain;
        images = new ArrayList<>();
        if (image != null && !image.equals("")) {
            images.add(Uri.parse(image));
            PhotoModel temp = new PhotoModel();
            temp.setImagePath(image);
            this.imagesString.add(0, temp);
        }
        for (int i = 0; i < imagesString.size(); i++) {
            if (imagesString.get(i).getImagePath() != null && !imagesString.get(i).getImagePath().equals("")) {
                images.add(Uri.parse(imagesString.get(i).getImagePath()));
                this.imagesString.add(imagesString.get(i));
            }
        }
        isEdit = true;
    }

    void addImage(Uri uri, PhotoModel stringImage) {
        if (isEdit)
            stringImage.setEdit(true);
        images.add(uri);
        imagesString.add(stringImage);
        this.notifyItemInserted(images.size() - 1);
    }

    public ArrayList<PhotoModel> getImagesString() {
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
        Log.e("removeImage: ", String.valueOf(imagesString.size()));
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

            if (isEdit)
                if (imagesString.get(getAdapterPosition()).getId() != null && !imagesString.get(getAdapterPosition()).getId().equals("")) {
                    ArrayList<String> string = mActivity.getDeletePhotos();
                    string.add(imagesString.get(getAdapterPosition()).getId());
                    mActivity.setDeletePhotos(string);
                }
            imagesString.remove(getAdapterPosition());
            images.remove(getAdapterPosition());


            notifyItemRemoved(position);
            notifyItemRangeChanged(getAdapterPosition(), images.size());
        }
    }
}