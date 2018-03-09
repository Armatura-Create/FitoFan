package com.example.alex.fitofan.ui.activity.preview_plan;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;

public class RecyclerAdapterPreviewPlan extends RecyclerView.Adapter<RecyclerAdapterPreviewPlan.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private int count;
    private PreviewPlanActivity mPreviewPlanActivity;


    RecyclerAdapterPreviewPlan(int count, PreviewPlanActivity mPreviewPlanActivity) {
        this.mPreviewPlanActivity = mPreviewPlanActivity;
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
                .inflate(R.layout.item_plan_preview, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;


    }

    void setImage(Uri uriExercise, ImageView imageExercise, CardView cvExercise) {
        cvExercise.setVisibility(View.VISIBLE);
        Glide.with(mPreviewPlanActivity.getContext()).load(uriExercise).into(imageExercise);
    }

    @Override
    public int getItemCount() {
        return count;
    }

}
