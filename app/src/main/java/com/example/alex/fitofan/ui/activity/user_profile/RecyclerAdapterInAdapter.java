package com.example.alex.fitofan.ui.activity.user_profile;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.alex.fitofan.R;

public class RecyclerAdapterInAdapter extends RecyclerView.Adapter<RecyclerAdapterInAdapter.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private UserProfileActivity mUserProfileActivity;


    RecyclerAdapterInAdapter(UserProfileActivity mUserProfileActivity) {
        this.mUserProfileActivity = mUserProfileActivity;
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
                        .inflate(R.layout.item_group, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        //header view

        //body view


        //header methods

        //body methods

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
