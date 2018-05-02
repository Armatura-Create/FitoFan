package com.example.alex.fitofan.ui.fragments.wall;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.FormatTime;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class RecyclerAdapterWall extends RecyclerView.Adapter<RecyclerAdapterWall.ViewHolder> implements LikeStatus {


    //Предоставляет ссылку на представления, используемые в RecyclerView
    private final RequestOptions mRequestOptions;
    private WallFragment mWallFragment;
    private ArrayList<GetTrainingModel> mWallModels;

    int numberOfClicks = 0;
    boolean threadStarted = false;
    final int DELAY_BETWEEN_CLICKS_IN_MILLISECONDS = 250;

    public ArrayList<GetTrainingModel> getmWallModels() {
        return mWallModels;
    }

    public void setmWallModels(ArrayList<GetTrainingModel> mWallModels) {
        this.mWallModels = mWallModels;
    }

    public RecyclerAdapterWall(ArrayList<GetTrainingModel> mWallModels, WallFragment mWallFragment) {
        this.mWallModels = mWallModels;
        this.mWallFragment = mWallFragment;
        mRequestOptions = new RequestOptions();
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
        TextView tvNameTrainig = linear.findViewById(R.id.tv_training_name);
        TextView tvTotalTime = linear.findViewById(R.id.tv_total_time);
        TextView tvTimeCreate = linear.findViewById(R.id.data_publication);
        TextView tvDescription = linear.findViewById(R.id.tv_description);

        LinearLayout userLiner = linear.findViewById(R.id.user_liner);
        LinearLayout planLiner = linear.findViewById(R.id.plan_liner);

        tvNameTrainig.setText(mWallModels.get(position).getName());
        tvTotalTime.setText(FormatTime.formatTime(Long.valueOf(mWallModels.get(position).getPlan_time())));
        tvTimeCreate.setText(mWallModels.get(position).getCreationDate());
        tvDescription.setText(mWallModels.get(position).getDescription());
        tvFirstName.setText(mWallModels.get(position).getUser().getName());
        tvLastName.setText(mWallModels.get(position).getUser().getSurname());

        Glide.with(mWallFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                .load(Uri.parse(mWallModels.get(position).getImage()))
                .apply(centerCropTransform())
                .transition(withCrossFade())
                .into(imageTrainingPlan);

        Glide.with(mWallFragment.getActivity().getApplicationContext())
                .load(Uri.parse(mWallModels.get(position).getUser().getImage_url()))
                .apply(centerCropTransform())
                .transition(withCrossFade())
                .into(imageUser); //ссылка на ImageView

        userLiner.setOnClickListener(v -> {
            mWallFragment.goUserProfile(mWallModels.get(position).getUser().getUid());
        });

        planLiner.setOnClickListener(v -> {
            ++numberOfClicks;
            if (!threadStarted) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        threadStarted = true;
                        try {
                            Thread.sleep(DELAY_BETWEEN_CLICKS_IN_MILLISECONDS);
                            if (numberOfClicks == 1) {
                                mWallFragment.goPreviewPlan(mWallModels.get(position).getId());
                            } else if (numberOfClicks == 2) {
                                if (mWallModels.get(position).getLiked() != 1) {
                                    mWallFragment.likePlan(mWallModels.get(position).getId());
                                }
                            }
                            numberOfClicks = 0;
                            threadStarted = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void onSuccess(Boolean info) {

    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public int getItemCount() {
        return mWallModels.size();
    }
}
