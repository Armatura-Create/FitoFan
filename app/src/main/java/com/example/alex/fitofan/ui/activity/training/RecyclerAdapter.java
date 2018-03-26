package com.example.alex.fitofan.ui.activity.training;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.FormatTime;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final RequestOptions mRequestOptions;
    //Предоставляет ссылку на представления, используемые в RecyclerView
    private TrainingActivity trainingActivity;
    private TrainingModel trainingModel;
    private TextView timer;

    public RecyclerAdapter(TrainingActivity trainingActivity, TrainingModel trainingModel) {
        mRequestOptions = new RequestOptions();
        this.trainingActivity = trainingActivity;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        ConstraintLayout linear = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timer, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final ConstraintLayout linear = holder.mLinearLayout;

        timer = linear.findViewById(R.id.tv_timer);

        TextView name = linear.findViewById(R.id.name_exercise);
        TextView description = linear.findViewById(R.id.description_exercise);
        ImageView imageExercise = linear.findViewById(R.id.image_exercise);
        TextView postDescription = linear.findViewById(R.id.back_name_exercise);
        TextView postTime = linear.findViewById(R.id.back_time_exercise);
        TextView nextDescription = linear.findViewById(R.id.next_name_exercise);
        TextView nextTime = linear.findViewById(R.id.next_time_exercise);
        TextView nextNextDescription = linear.findViewById(R.id.next_next_name_exercise);
        TextView nextNextTime = linear.findViewById(R.id.next_next_time_exercise);

        timer.setTag(position);

        timer.setText(FormatTime.formatTime(trainingModel.getExercises().get(position).getTime()));

        name.setText(trainingModel.getExercises().get(position).getName());
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setText(trainingModel.getExercises().get(position).getDescription());

        if (trainingModel.getExercises().get(position).getImage() != null) {
            Glide.with(trainingActivity.getContext())
                    .load(Uri.parse(trainingModel.getExercises().get(position).getImage()))
                    .apply(mRequestOptions)
                    .into(imageExercise);
        }

        if (position > 0) {
            postDescription.setText(trainingModel.getExercises().get(position - 1).getName());
            postTime.setText(FormatTime.formatTime(
                    trainingModel.getExercises().get(position - 1).getTime()
            ));
        } else {
            postDescription.setText("");
            postTime.setText("");
        }

        if (position < getItemCount() - 1) {
            if (position < getItemCount() - 2) {
                nextNextDescription.setText(
                        trainingModel.getExercises().get(position + 2).getName()
                );
                nextNextTime.setText(FormatTime.formatTime(
                        trainingModel.getExercises().get(position + 2).getTime()));
            } else {
                nextNextDescription.setText("");
                nextNextTime.setText("");
            }

            nextDescription.setText(trainingModel.getExercises().get(position + 1).getName());
            nextTime.setText(FormatTime.formatTime(
                    trainingModel.getExercises().get(position + 1).getTime()));
        } else {
            nextDescription.setText("");
            nextTime.setText("");
            nextNextDescription.setText("");
            nextNextTime.setText("");
        }

    }

    void setTime(long time, int position){
        Log.e( "setTime: ", timer.getTag(0).toString());
    }

    @Override
    public int getItemCount() {
        return trainingModel.getExercises() != null ? trainingModel.getExercises().size() : 0;
    }
}
