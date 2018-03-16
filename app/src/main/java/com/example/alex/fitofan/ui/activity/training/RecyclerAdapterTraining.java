package com.example.alex.fitofan.ui.activity.training;

import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.utils.FormatTime;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterTraining extends RecyclerView.Adapter<RecyclerAdapterTraining.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private ArrayList<ExerciseModel> mExerciseModel;
    private TrainingActivity mTrainingActivity;
    private int mapPosition = 0;

    public RecyclerAdapterTraining(ArrayList<ExerciseModel> mExerciseModel, TrainingActivity mTrainingActivity) {
        this.mExerciseModel = mExerciseModel;
        this.mTrainingActivity = mTrainingActivity;
    }

    public int getMapPosition() {
        return mapPosition;
    }

    public void setMapPosition(int mapPosition) {
        this.mapPosition = mapPosition;
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
                .inflate(R.layout.part_training_map, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final ConstraintLayout linear = holder.mLinearLayout;

        CircleImageView imageViewItemPic = linear.findViewById(R.id.map_road_image);
        TextView textViewTotalTime = linear.findViewById(R.id.total_time_exercise);
        TextView textViewItemDescription = linear.findViewById(R.id.current_exercise);
        LinearLayout line = linear.findViewById(R.id.line);

        textViewTotalTime.setText(FormatTime.formatTime(mExerciseModel.get(position).getTime()));
        textViewItemDescription.setText(mExerciseModel.get(position).getName());

        if(position > mapPosition) {
            setParamsSmall(imageViewItemPic, textViewTotalTime, textViewItemDescription, 2);
        } else  if(position == mapPosition){
            setParamsBig(imageViewItemPic, textViewTotalTime, textViewItemDescription);
        } else {
            setParamsSmall(imageViewItemPic, textViewTotalTime, textViewItemDescription, 1);
        }

        if (position == 0) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) line.getLayoutParams();
            lp.setMargins(0, 45, 0, 0);
        }
        if (position == mExerciseModel.size() - 1) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) line.getLayoutParams();
            lp.setMargins(0, 0, 0, 45);
        }
    }

    private void setParamsSmall(CircleImageView imageViewItemPic, TextView textViewTotalTime, TextView textViewItemDescription, int color) {
        if (color == 1)
            imageViewItemPic.setImageDrawable(mTrainingActivity.getResources().getDrawable(R.drawable.background_blue));
        else
            imageViewItemPic.setImageDrawable(mTrainingActivity.getResources().getDrawable(R.drawable.background_white));
        textViewTotalTime.setTextSize(16);
        textViewItemDescription.setTextSize(16);
        textViewItemDescription.setTypeface(null, Typeface.NORMAL);
        textViewTotalTime.setTypeface(null, Typeface.NORMAL);
    }

    private void setParamsBig(CircleImageView imageViewItemPic, TextView textViewTotalTime, TextView textViewItemDescription) {
        imageViewItemPic.setImageDrawable(mTrainingActivity.getResources().getDrawable(R.drawable.background_red));
        textViewTotalTime.setTextSize(24);
        textViewItemDescription.setTextSize(24);
        textViewItemDescription.setTypeface(null, Typeface.BOLD);
        textViewTotalTime.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public int getItemCount() {
        return mExerciseModel.size();
    }
}
