package com.example.alex.fitofan.ui.activity.preview_plan;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.FormatTime;

public class RecyclerAdapterPreviewPlan extends RecyclerView.Adapter<RecyclerAdapterPreviewPlan.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private TrainingModel trainingModel;
    private PreviewPlanActivity mPreviewPlanActivity;
    private int isGoTo;


    RecyclerAdapterPreviewPlan(TrainingModel trainingModel, PreviewPlanActivity mPreviewPlanActivity, int isGoTo) {
        this.mPreviewPlanActivity = mPreviewPlanActivity;
        this.trainingModel = trainingModel;
        this.isGoTo = isGoTo;
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
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position != getItemCount() - 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = null;
        switch (viewType) {
            case 0:
                linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_preview_header, parent, false);
                break;
            case 1:
                linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_plan_preview, parent, false);
                break;
            case 2:
                linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_go_training, parent, false);
                break;
        }
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        //header
        if (position == 0) {
            TextView nameTraining = linear.findViewById(R.id.tv_training_name);
            TextView timeTraining = linear.findViewById(R.id.tv_total_time);

            nameTraining.setText(trainingModel.getName());
            timeTraining.setText(FormatTime.formatTime(trainingModel.getTime()));
        }

        //body
        if (position > 0 && position != getItemCount() - 1) {
            if (isGoTo == 2)
                linear.findViewById(R.id.tv_add_audio_exercise).setVisibility(View.GONE);

            TextView nameExercise = linear.findViewById(R.id.name_exercise);
            TextView countRepetition = linear.findViewById(R.id.time_repetitions);
            TextView descriptionExercise = linear.findViewById(R.id.description_exercise);
            TextView timeRelax = linear.findViewById(R.id.time_relax);
            ImageView imageExercise = linear.findViewById(R.id.image_exercise_plan);

            nameExercise.setText(trainingModel.getExercises().get(position - 1).getName());
            if (trainingModel.getExercises().get(position - 1).getCountRepetitions() > 0) {
                countRepetition.setText(String.valueOf(trainingModel.getExercises().get(position - 1).getCountRepetitions()));
            } else {
                countRepetition.setText(FormatTime.formatTime(trainingModel.getExercises().get(position - 1).getTime()));
            }
            descriptionExercise.setText(trainingModel.getExercises().get(position - 1).getDescription());
            timeRelax.setText(FormatTime.formatTime(trainingModel.getExercises().get(position - 1).getTimeBetween()));

            if (trainingModel.getExercises().get(position - 1).getImage() != null) {
                Glide.with(mPreviewPlanActivity.getContext())
                        .load(Uri.parse(trainingModel.getExercises().get(position - 1).getImage()))
                        .placeholder(R.mipmap.icon)
                        .fitCenter()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageExercise);
            } else {
                Toast.makeText(mPreviewPlanActivity.getContext(), "Изображение уражнения №" + (position) + " Не найденно", Toast.LENGTH_SHORT).show();
            }
        }

        //footer
        if (position == getItemCount() - 1) {
            if (isGoTo == 2)
                linear.findViewById(R.id.bt_bottom_preview).setOnClickListener(v -> {
                    mPreviewPlanActivity.goTraining();
                });
        }
    }

    @Override
    public int getItemCount() {
        return trainingModel.getExercises() == null ? 0 : trainingModel.getExercises().size() + 2;
    }

}
