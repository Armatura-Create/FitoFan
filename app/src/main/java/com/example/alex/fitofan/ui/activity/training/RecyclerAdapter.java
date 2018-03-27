package com.example.alex.fitofan.ui.activity.training;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.TrainingModel;

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
                .inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

//        TextView name = linear.findViewById(R.id.name_exercise);
//        TextView description = linear.findViewById(R.id.description_exercise);
//        ImageView imageExercise = linear.findViewById(R.id.image_exercise);
//        TextView postDescription = linear.findViewById(R.id.back_name_exercise);
//        TextView postTime = linear.findViewById(R.id.back_time_exercise);
//        TextView nextDescription = linear.findViewById(R.id.next_name_exercise);
//        TextView nextTime = linear.findViewById(R.id.next_time_exercise);
//        TextView nextNextDescription = linear.findViewById(R.id.next_next_name_exercise);
//        TextView nextNextTime = linear.findViewById(R.id.next_next_time_exercise);


    }

    private int countingExercises() {
        int result = 0;
        if (trainingModel.getExercises() != null) {
            for (int i = 0; i < trainingModel.getExercises().size(); i++) {
                result += trainingModel.getExercises().get(i).getCountRepetitions() * 2;
            }
            return result;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return countingExercises();
    }
}
