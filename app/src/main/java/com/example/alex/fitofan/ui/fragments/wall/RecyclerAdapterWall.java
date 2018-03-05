package com.example.alex.fitofan.ui.fragments.wall;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.WallModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterWall extends RecyclerView.Adapter<RecyclerAdapterWall.ViewHolder> {


    //Предоставляет ссылку на представления, используемые в RecyclerView

    private WallFragment mWallFragment;
    private ArrayList<WallModel> mWallModels;

    public RecyclerAdapterWall(ArrayList<WallModel> mWallModels, WallFragment mWallFragment) {
        this.mWallModels = mWallModels;
        this.mWallFragment = mWallFragment;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //Определение класса ViewHolder
        private LinearLayout mLinearLayout;

        ViewHolder(LinearLayout v) {
            super(v);
            mLinearLayout = v;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wall, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        CircleImageView imageUser = linear.findViewById(R.id.image_user);
        ImageView imageTrainingPlan = linear.findViewById(R.id.image_training_plan_wall);
        TextView tvFirstName = linear.findViewById(R.id.first_name_wall);
        TextView tvLastName = linear.findViewById(R.id.last_name_wall);

        Uri uri1 = Uri.parse(mWallModels.get(position).getImageTraining());
        Glide.with(mWallFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                .load(uri1)
                .fitCenter()
                .thumbnail(0.5f)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.mipmap.icon)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageTrainingPlan); //ссылка на ImageView

        Uri uri2 = Uri.parse("http://backbreaker.net/wp-content/uploads/2015/11/1295992106_brad_pitt.jpg");
        Glide.with(mWallFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                .load(uri2)
                .fitCenter()
                .thumbnail(0.5f)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.mipmap.icon)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageUser); //ссылка на ImageView

    }

    @Override
    public int getItemCount() {
        return mWallModels.size();
    }

}