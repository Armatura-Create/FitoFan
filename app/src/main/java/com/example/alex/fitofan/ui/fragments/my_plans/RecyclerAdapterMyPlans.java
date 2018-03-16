package com.example.alex.fitofan.ui.fragments.my_plans;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        TextView name = linear.findViewById(R.id.tv_training_name_my_plan);
        TextView description = linear.findViewById(R.id.tv_description_my_plan);
        TextView time = linear.findViewById(R.id.tv_total_time);

        name.setText(mTrainings.get(position).getName());
        description.setText(mTrainings.get(position).getDescription());
        time.setText(FormatTime.formatTime(mTrainings.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        assert mTrainings != null;
        return mTrainings.size();
    }
}
