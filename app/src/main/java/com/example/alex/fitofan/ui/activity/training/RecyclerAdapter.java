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
import com.example.alex.fitofan.models.ExerciseModelFromTraining;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.FormatTime;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final RequestOptions mRequestOptions;
    //Предоставляет ссылку на представления, используемые в RecyclerView
    private TrainingActivity trainingActivity;
    private ArrayList<ExerciseModelFromTraining> model;

    public RecyclerAdapter(TrainingActivity trainingActivity, ArrayList<ExerciseModelFromTraining> model) {
        mRequestOptions = new RequestOptions();
        this.trainingActivity = trainingActivity;
        this.model = model;
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

        TextView name = linear.findViewById(R.id.tv_name_exercise);
        TextView time = linear.findViewById(R.id.tv_time);
        TextView number = linear.findViewById(R.id.tv_number);

        name.setText(model.get(position).getName());
        time.setText(FormatTime.formatTime(model.get(position).getTime()));
        number.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return model != null ? model.size() : 0;
    }
}
