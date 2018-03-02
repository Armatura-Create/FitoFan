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

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterWall extends RecyclerView.Adapter<RecyclerAdapterWall.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView

    private WallFragment mWallFragment;
    private int count;

    public RecyclerAdapterWall(int count, WallFragment mWallFragment) {
        this.count = count;
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

        Uri uri1 = Uri.parse("http://bipbap.ru/wp-content/uploads/2017/04/72fqw2qq3kxh.jpg");
        Glide.with(mWallFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                .load(uri1)
                .fitCenter()
                .thumbnail(0.5f)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageTrainingPlan); //ссылка на ImageView

        Uri uri2 = Uri.parse("http://backbreaker.net/wp-content/uploads/2015/11/1295992106_brad_pitt.jpg");
        Glide.with(mWallFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                .load(uri2)
                .fitCenter()
                .thumbnail(0.5f)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageUser); //ссылка на ImageView

    }

    @Override
    public int getItemCount() {
        return count;
    }

}
