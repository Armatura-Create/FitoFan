package com.example.alex.fitofan.ui.fragments.my_plans;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.utils.ActionPlanCard;
import com.example.alex.fitofan.utils.CountData;
import com.example.alex.fitofan.utils.FormatTime;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class RecyclerAdapterMyPlans extends RecyclerView.Adapter<RecyclerAdapterMyPlans.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView

    private MyPlansFragment mMyPlansFragment;
    private ArrayList<GetTrainingModel> mTrainings;

    public ArrayList<GetTrainingModel> getTrainings() {
        return mTrainings;
    }

    public void setmTrainings(ArrayList<GetTrainingModel> mTrainings) {
        this.mTrainings = mTrainings;
    }

    private boolean isMy = true;

    private int numberOfClicks = 0;
    private boolean threadStarted = false;
    private final int DELAY_BETWEEN_CLICKS_IN_MILLISECONDS = 250;

    RecyclerAdapterMyPlans(ArrayList<GetTrainingModel> trainings, MyPlansFragment mMyPlansFragment) {
        mTrainings = trainings;
        this.mMyPlansFragment = mMyPlansFragment;
    }

    public void setTrainings(ArrayList<GetTrainingModel> trainings) {
        mTrainings = trainings;
    }

    public boolean getMy() {
        return isMy;
    }

    public void setMy(boolean my) {
        isMy = my;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //Определение класса ViewHolder
        private LinearLayout mLinearLayout;

        ViewHolder(LinearLayout v) {
            super(v);
            mLinearLayout = v;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(linear);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        TextView name = linear.findViewById(R.id.tv_training_name);
        TextView description = linear.findViewById(R.id.tv_description);
        TextView time = linear.findViewById(R.id.tv_total_time);
        TextView countLike = linear.findViewById(R.id.count_like);
        ImageView imageTraining = linear.findViewById(R.id.image_training);
        TextView countComments = linear.findViewById(R.id.count_comments);
        TextView countSaved = linear.findViewById(R.id.saved_plan);

        imageTraining.setImageDrawable(null);

        ImageView sharePlan = linear.findViewById(R.id.icon_send_plan);
        ImageView save = linear.findViewById(R.id.icon_save);
        ImageView like = linear.findViewById(R.id.icon_like);
        ImageView comments = linear.findViewById(R.id.icon_comments);
        LinearLayout planLinear = linear.findViewById(R.id.plan_liner);
        CoordinatorLayout likeCoor= linear.findViewById(R.id.coordinator_like);
        CoordinatorLayout comCoor= linear.findViewById(R.id.coordinator_comments);
        CoordinatorLayout saveCoor= linear.findViewById(R.id.coordinator_save);

        name.setText(mTrainings.get(position).getName());
        description.setText(mTrainings.get(position).getDescription());
        time.setText(FormatTime.formatTime(Long.parseLong(mTrainings.get(position).getPlan_time())));

        like.setImageDrawable(mMyPlansFragment.getResources().getDrawable(R.drawable.ic_favorite_black));
        save.setImageDrawable(mMyPlansFragment.getResources().getDrawable(R.drawable.ic_save_black));

        countSaved.setText(CountData.mathLikes(mTrainings.get(position).getSaved()));
        countLike.setText(CountData.mathLikes(mTrainings.get(position).getLikes()));
        countComments.setText(CountData.mathLikes(mTrainings.get(position).getComments()));
        countLike.setTextColor(Color.parseColor("#000000"));

        if (mTrainings.get(position).getLiked() == 1) {
            like.setImageDrawable(mMyPlansFragment.getResources().getDrawable(R.drawable.ic_favorite_full_red));
            countLike.setTextColor(Color.parseColor("#ffffff"));
        }
        if (mTrainings.get(position).getIsSaved() == 1) {
            save.setImageDrawable(mMyPlansFragment.getResources().getDrawable(R.drawable.ic_save_full_black));
        }

        if (mTrainings.get(position).getStatus().equals("1")) {
            likeCoor.setVisibility(View.VISIBLE);
            saveCoor.setVisibility(View.VISIBLE);
            comCoor.setVisibility(View.VISIBLE);
            sharePlan.setVisibility(View.GONE);
        } else {
            sharePlan.setVisibility(View.VISIBLE);
            likeCoor.setVisibility(View.GONE);
            saveCoor.setVisibility(View.GONE);
            comCoor.setVisibility(View.GONE);
        }

        if (mTrainings.get(position).getImage() != null) {
            Glide.with(mMyPlansFragment.getContext())
                    .load(Uri.parse(mTrainings.get(position).getImage()))
                    .apply(centerCropTransform())
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .transition(withCrossFade())
                    .into(imageTraining);
        }

        sharePlan.setOnClickListener(view -> {
            mMyPlansFragment.publicationPlan(mTrainings.get(position).getId());
        });

        planLinear.setOnClickListener(v -> {
            ++numberOfClicks;
            if (!threadStarted) {
                new Thread(() -> {
                    threadStarted = true;
                    try {
                        Thread.sleep(DELAY_BETWEEN_CLICKS_IN_MILLISECONDS);
                        if (numberOfClicks == 1) {
                            mMyPlansFragment.goToPreview(
                                    mTrainings.get(position).getId(),
                                    mTrainings.get(position).getUserId());
                        } else if (numberOfClicks == 2) {
                            mMyPlansFragment.likePlan(String.valueOf(mTrainings.get(position).getId()),
                                    like, countLike, false, position);
                        }
                        numberOfClicks = 0;
                        threadStarted = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        like.setOnClickListener(view -> {
            mMyPlansFragment.likePlan(String.valueOf(mTrainings.get(position).getId()), like, countLike, true, position);
        });

        save.setOnClickListener(view -> {
            mMyPlansFragment.savePlan(String.valueOf(mTrainings.get(position).getId()), save, position, countSaved);
        });

        comments.setOnClickListener(view -> {
            ActionPlanCard.goComments(mMyPlansFragment.getContext(),
                    String.valueOf(mTrainings.get(position).getId()), mTrainings.get(position).getUserId());
        });
    }

    @Override
    public int getItemCount() {
        return mTrainings == null ? 0 : mTrainings.size();
    }
}
