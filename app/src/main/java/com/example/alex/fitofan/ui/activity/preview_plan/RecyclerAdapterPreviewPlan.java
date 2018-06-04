package com.example.alex.fitofan.ui.activity.preview_plan;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.CountDataOnPreviewModel;
import com.example.alex.fitofan.models.ExerciseModelFromTraining;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.ActionPlanCard;
import com.example.alex.fitofan.utils.CountData;
import com.example.alex.fitofan.utils.FormatTime;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class RecyclerAdapterPreviewPlan extends RecyclerView.Adapter<RecyclerAdapterPreviewPlan.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private PreviewPlanActivity trainingActivity;
    private User mUserModel;
    private ArrayList<ExerciseModelFromTraining> model;
    private TrainingModel trainingModel;

    public PreviewPlanActivity getTrainingActivity() {
        return trainingActivity;
    }

    public void setTrainingActivity(PreviewPlanActivity trainingActivity) {
        this.trainingActivity = trainingActivity;
    }

    public void setModel(ArrayList<ExerciseModelFromTraining> model) {
        this.model = model;
    }

    public RecyclerAdapterPreviewPlan(PreviewPlanActivity trainingActivity, ArrayList<ExerciseModelFromTraining> model) {
        this.trainingActivity = trainingActivity;
        this.model = model;
    }

    public ArrayList<ExerciseModelFromTraining> getModel() {
        return model;
    }

    public User getUserModel() {
        return mUserModel;
    }

    public void setUserModel(User mUserModel) {
        this.mUserModel = mUserModel;
    }

    public TrainingModel getTrainingModel() {
        return trainingModel;
    }

    public void setTrainingModel(TrainingModel trainingModel) {
        this.trainingModel = trainingModel;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //Определение класса ViewHolder
        private ConstraintLayout mLinearLayout;

        ViewHolder(ConstraintLayout v) {
            super(v);
            mLinearLayout = v;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        ConstraintLayout linear = null;
        switch (viewType) {
            case 0:
                linear = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_preview_header, parent, false);
                break;
            case 1:
                linear = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_plan_preview, parent, false);
                break;
        }
        return new ViewHolder(linear);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final ConstraintLayout linear = holder.mLinearLayout;

        if (position == 0) {
            TextView createdDate = linear.findViewById(R.id.created_date);
            TextView descriptionTraning = linear.findViewById(R.id.tv_training_description);
            TextView userName = linear.findViewById(R.id.name_user);

            TextView time = linear.findViewById(R.id.time);
            TextView distance = linear.findViewById(R.id.distance);
            TextView count = linear.findViewById(R.id.count);
            TextView weight = linear.findViewById(R.id.weight);
            TextView distanceParam = linear.findViewById(R.id.distance_text);
            TextView weightParam = linear.findViewById(R.id.weight_text);

            ImageView userImage = linear.findViewById(R.id.image_user);

            if (trainingModel != null) {
                CountDataOnPreviewModel model = CountData.mathData(trainingModel);

                descriptionTraning.setText(trainingModel.getDescription());

                time.setText(model.getTime());
                distance.setText(model.getDistance());
                count.setText(model.getCount());
                weight.setText(model.getWeight());
                distanceParam.setText(model.getDistanceParam());
                weightParam.setText(model.getWeightParam());

                createdDate.setText(trainingModel.getCreatedTime());
            }
            if (mUserModel != null) {
                userName.setText(mUserModel.getName() + " " + mUserModel.getSurname());

                Glide.with(trainingActivity.getContext())
                        .load(Uri.parse(mUserModel.getImage_url()))
                        .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                        .transition(withCrossFade())
                        .into(userImage);

                userImage.setOnClickListener(view -> {
                    ActionPlanCard.goUserProfile(trainingActivity.getContext(), mUserModel.getUid());
                });

                if (mUserModel.getUid().equals(
                        new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
                ))
                    createdDate.setVisibility(View.GONE);
            }
        }

        if (position >= 1) {

            TextView name = linear.findViewById(R.id.tv_name_exercise);
            TextView time = linear.findViewById(R.id.tv_count_plans);
            TextView description = linear.findViewById(R.id.tv_description_exercise);
            ImageView image = linear.findViewById(R.id.image_exercise);

            name.setText(model.get(position - 1).getName());
            description.setText(model.get(position - 1).getDescription());

            if (!model.get(position - 1).isRest())
                time.setText(FormatTime.formatCountWithDimension(model.get(position - 1).getTime()));
            else
                time.setText(FormatTime.formatTime(model.get(position - 1).getTime()));

            if (!model.get(position - 1).isRest() && model.get(position - 1).getImage() != null) {
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(trainingActivity.getContext())
                        .load(Uri.parse(model.get(position - 1).getImage()))
                        .apply(placeholderOf(R.drawable.logo_fitofan))
                        .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                        .transition(withCrossFade())
                        .into(image);
            } else if (model.get(position - 1).isRest()) {
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(trainingActivity.getContext())
                        .load(R.mipmap.logo_fitofan)
                        .apply(placeholderOf(R.mipmap.logo_fitofan))
                        .transition(withCrossFade())
                        .into(image);
            } else {
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(trainingActivity.getContext())
                        .load(R.mipmap.logo_fitofan_old)
                        .apply(placeholderOf(R.mipmap.logo_fitofan_old))
                        .transition(withCrossFade())
                        .into(image);
            }

        }
    }

    @Override
    public int getItemCount() {
        return model != null ? model.size() + 1 : 0;
    }
}
