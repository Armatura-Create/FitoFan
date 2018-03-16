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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan_preview, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        if (isGoTo == 2)
            linear.findViewById(R.id.tv_add_audio_exercise).setVisibility(View.GONE);

        TextView nameExercise = linear.findViewById(R.id.name_exercise);
        TextView countRepetition = linear.findViewById(R.id.time_repetitions);
        TextView descriptionExercise = linear.findViewById(R.id.description_exercise);
        TextView timeRelax = linear.findViewById(R.id.time_relax);
        ImageView imageExercise = linear.findViewById(R.id.image_exercise_plan);

        nameExercise.setText(trainingModel.getExercises().get(position).getName());
        if(trainingModel.getExercises().get(position).getCountRepetitions() > 0){
            countRepetition.setText(String.valueOf(trainingModel.getExercises().get(position).getCountRepetitions()));
        } else {
            countRepetition.setText(FormatTime.formatTime(trainingModel.getExercises().get(position).getTime()));
        }
        descriptionExercise.setText(trainingModel.getExercises().get(position).getDescription());
        timeRelax.setText(FormatTime.formatTime(trainingModel.getExercises().get(position).getTimeBetween()));

        if(trainingModel.getExercises().get(position).getImage() != null) {
            Glide.with(mPreviewPlanActivity.getContext())
                    .load(Uri.parse(trainingModel.getExercises().get(position).getImage()))
                    .placeholder(R.mipmap.icon)
                    .fitCenter()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageExercise);
        } else {
            Toast.makeText(mPreviewPlanActivity.getContext(), "Изображение уражнения №" + (position + 1) + "Не найденно", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return trainingModel.getExercises() == null ? 0 : trainingModel.getExercises().size();
    }

}
