package com.example.alex.fitofan.ui.fragments.wall;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.utils.ActionPlanCard;
import com.example.alex.fitofan.utils.FormatTime;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

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
        TextView countLike = linear.findViewById(R.id.count_like);

        LinearLayout userLiner = linear.findViewById(R.id.user_liner);
        LinearLayout planLiner = linear.findViewById(R.id.plan_liner);

        ImageView like = linear.findViewById(R.id.icon_like);
        ImageView save = linear.findViewById(R.id.icon_save);
        ImageView comments = linear.findViewById(R.id.icon_comments);

        countLike.setText(mWallFragment.getResources().getString(R.string.like) + ": " + mWallModels.get(position).getLikes());
        tvNameTrainig.setText(mWallModels.get(position).getName());
        tvTotalTime.setText(FormatTime.formatTime(Long.valueOf(mWallModels.get(position).getPlan_time())));
        tvTimeCreate.setText(mWallModels.get(position).getCreationDate());
        tvDescription.setText(mWallModels.get(position).getDescription());
        tvFirstName.setText(mWallModels.get(position).getUser().getName());
        tvLastName.setText(mWallModels.get(position).getUser().getSurname());

        like.setImageDrawable(mWallFragment.getResources().getDrawable(R.drawable.ic_favorite_black));
        save.setImageDrawable(mWallFragment.getResources().getDrawable(R.drawable.ic_save_black));

        if (mWallModels.get(position).getLiked() == 1) {
            like.setImageDrawable(mWallFragment.getResources().getDrawable(R.drawable.ic_favorite_full_red));
        }
        if (mWallModels.get(position).getIsSaved() == 1) {
            save.setImageDrawable(mWallFragment.getResources().getDrawable(R.drawable.ic_save_full_black));
        }

        Glide.with(mWallFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                .load(Uri.parse(mWallModels.get(position).getImage()))
                .apply(centerCropTransform())
                .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .transition(withCrossFade())
                .into(imageTrainingPlan);

        Glide.with(mWallFragment.getActivity().getApplicationContext())
                .load(Uri.parse(mWallModels.get(position).getUser().getImage_url()))
                .apply(centerCropTransform())
                .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .transition(withCrossFade())
                .into(imageUser); //ссылка на ImageView

        userLiner.setOnClickListener(v -> {
            ActionPlanCard.goUserProfile(mWallFragment.getContext(), mWallModels.get(position).getUser().getUid());
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
                                ActionPlanCard.goPreviewPlan(mWallFragment.getContext(), mWallModels.get(position).getId(),
                                        mWallModels.get(position).getUserId());
                            } else if (numberOfClicks == 2) {
                                mWallFragment.likePlan(mWallModels.get(position).getId(), like, countLike, false, position);
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

        like.setOnClickListener(view -> {
            mWallFragment.likePlan(mWallModels.get(position).getId(), like, countLike, true, position);
        });

        save.setOnClickListener(view -> {
            mWallFragment.savePlan(mWallModels.get(position).getId(), save, position);
        });

        comments.setOnClickListener(view -> {
            ActionPlanCard.goComments(mWallFragment.getContext(), mWallModels.get(position).getId(), mWallModels.get(position).getUserId());
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
