package com.example.alex.fitofan.ui.activity.user_profile;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.alex.fitofan.R;

public class RecyclerAdapterUserProfile extends RecyclerView.Adapter<RecyclerAdapterUserProfile.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private UserProfileActivity mUserProfileActivity;


    RecyclerAdapterUserProfile(UserProfileActivity mUserProfileActivity) {
        this.mUserProfileActivity = mUserProfileActivity;
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
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        if (position == 1)
            return 1;
        else
            return 2;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        ConstraintLayout linear = null;
        switch (viewType) {
            case 0:
                linear = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user_data, parent, false);
                break;
            case 1:
                linear = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user_sub, parent, false);
                break;
            case 2:
                linear = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user_trainig, parent, false);
                break;
        }
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final ConstraintLayout linear = holder.mLinearLayout;

        Log.e("onBindViewHolder: ", String.valueOf(getItemCount()));

        //header view

        //body view


        //header methods

        //body methods

    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
