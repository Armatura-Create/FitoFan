package com.example.alex.fitofan.ui.activity.create_plan;

import android.app.Dialog;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.utils.CustomDialog;

import java.util.ArrayList;

public class RecyclerAdapterCreatePlan extends RecyclerView.Adapter<RecyclerAdapterCreatePlan.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private int count;
    private CreatePlanActivity mCreatePlanActivity;


    public RecyclerAdapterCreatePlan(int count, CreatePlanActivity mCreatePlanActivity) {
        this.mCreatePlanActivity = mCreatePlanActivity;
        this.count = count;
    }

    public int getСount() {
        return count;
    }

    public void setСount(int count) {
        this.count = count;
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
                .inflate(R.layout.item_create_plan, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        TextView delItemPlan = linear.findViewById(R.id.tv_cancel_item_create_plan);
        View delimiter = linear.findViewById(R.id.delimiter_exercise);
        EditText etNameExercise = linear.findViewById(R.id.et_exercise_name);
        MaterialRippleLayout btAddImage = linear.findViewById(R.id.bt_add_image_exercise);
        ImageView image = linear.findViewById(R.id.image_exercise_create);
        CardView cvImage = linear.findViewById(R.id.image_exercise_card);

        //разделитель и кнопка удаления упражнения
        if (position < count - 1) {
            delItemPlan.setVisibility(View.GONE);
            delimiter.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
            delItemPlan.setVisibility(View.GONE);
            delimiter.setVisibility(View.GONE);
            if (count > 1){
                delimiter.setVisibility(View.VISIBLE);
            }
        } else if (position == count - 1) {
            delItemPlan.setVisibility(View.VISIBLE);
            delimiter.setVisibility(View.GONE);
        }

        delItemPlan.setOnClickListener(v -> {
            mCreatePlanActivity.delItemExercise(position);
        });

        etNameExercise.setOnClickListener(v -> {
            Dialog dialog = CustomDialog.dialogTime(mCreatePlanActivity.getContext(), "Time", "time time time", "Save");
//           etNameExercise.setText();
        });

        btAddImage.setOnClickListener(v -> {
            mCreatePlanActivity.addImageExercise(position, image, cvImage);
        });


    }

    void setImage(Uri uriExercise, ImageView imageExercise, CardView cvExercise) {
        cvExercise.setVisibility(View.VISIBLE);
        Glide.with(mCreatePlanActivity.getContext()).load(uriExercise).into(imageExercise);
    }

    @Override
    public int getItemCount() {
        return count;
    }

}
