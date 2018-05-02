package com.example.alex.fitofan.ui.activity.preview_plan;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.ExerciseModelFromTraining;
import com.example.alex.fitofan.utils.FormatTime;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class RecyclerAdapterPreviewPlan extends RecyclerView.Adapter<RecyclerAdapterPreviewPlan.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private PreviewPlanActivity trainingActivity;

    private ArrayList<ExerciseModelFromTraining> model;

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
                .inflate(R.layout.item_plan_preview, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        TextView name = linear.findViewById(R.id.tv_name_exercise);
        TextView time = linear.findViewById(R.id.tv_count_plans);
        TextView description = linear.findViewById(R.id.tv_description_exercise);
        ImageView image = linear.findViewById(R.id.image_exercise);

        name.setText(model.get(position).getName());
        description.setText(model.get(position).getDescription());

        if (!model.get(position).isRest())
            time.setText(FormatTime.formatCountWithDimension(model.get(position).getTime()));
        else
            time.setText(FormatTime.formatTime(model.get(position).getTime()));

        if (!model.get(position).isRest() && model.get(position).getImage() != null) {
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(trainingActivity.getContext())
                    .load(Uri.parse(model.get(position).getImage()))
                    .apply(placeholderOf(R.drawable.logo_fitofan))
                    .transition(withCrossFade())
                    .into(image);
        } else if (model.get(position).isRest()) {
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

    @Override
    public int getItemCount() {
        return model != null ? model.size() : 0;
    }
}
