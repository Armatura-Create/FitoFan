package com.example.alex.fitofan.ui.fragments.my_plans;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.alex.fitofan.R;

public class RecyclerAdapterMyPlans extends RecyclerView.Adapter<RecyclerAdapterMyPlans.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView

    private MyPlansFragment mMyPlansFragment;
    private int count;

    public RecyclerAdapterMyPlans(int count, MyPlansFragment mMyPlansFragment) {
        this.count = count;
        this.mMyPlansFragment = mMyPlansFragment;
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



    }

    @Override
    public int getItemCount() {
        return count;
    }

}
