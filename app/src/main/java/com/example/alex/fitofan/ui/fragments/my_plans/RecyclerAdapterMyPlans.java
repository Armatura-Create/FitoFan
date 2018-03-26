package com.example.alex.fitofan.ui.fragments.my_plans;

import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.FormatTime;

import java.util.ArrayList;

public class RecyclerAdapterMyPlans extends RecyclerView.Adapter<RecyclerAdapterMyPlans.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView

    private MyPlansFragment mMyPlansFragment;
    private ArrayList<TrainingModel> mTrainings;

    public RecyclerAdapterMyPlans(ArrayList<TrainingModel> trainings, MyPlansFragment mMyPlansFragment) {
        mTrainings = trainings;
        this.mMyPlansFragment = mMyPlansFragment;
    }

    public ArrayList<TrainingModel> getTrainings() {
        return mTrainings;
    }

    public void setTrainings(ArrayList<TrainingModel> trainings) {
        mTrainings = trainings;
        super.notifyDataSetChanged();
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
                .inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        TextView name = linear.findViewById(R.id.tv_training_name);
        TextView description = linear.findViewById(R.id.tv_description);
        TextView time = linear.findViewById(R.id.tv_total_time);
        ImageView imageTraining = linear.findViewById(R.id.image_training);

        name.setText(mTrainings.get(position).getName());
        description.setText(mTrainings.get(position).getDescription());
        time.setText(FormatTime.formatTime(mTrainings.get(position).getTime()));

        if (mTrainings.get(position).getImage() != null) {
            Glide.with(mMyPlansFragment.getContext())
                    .load(Uri.parse(mTrainings.get(position).getImage()))
                    .into(imageTraining);
        }
    }

    @Override
    public int getItemCount() {
        assert mTrainings != null;
        return mTrainings.size();
    }
}
