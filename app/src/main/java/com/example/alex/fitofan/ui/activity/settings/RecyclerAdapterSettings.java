package com.example.alex.fitofan.ui.activity.settings;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.SettingsModel;

public class RecyclerAdapterSettings extends RecyclerView.Adapter<RecyclerAdapterSettings.LinearSet> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private SettingActivity trainingActivity;
    private SettingsModel model;

    public RecyclerAdapterSettings(SettingActivity trainingActivity) {
        this.trainingActivity = trainingActivity;
        model = new SettingsModel();
    }

    @NonNull
    @Override
    public LinearSet onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        ConstraintLayout linear = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings, parent, false);
        return new LinearSet(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull LinearSet holder, final int position) {
        //Заполнение заданного представления данными

        holder.description.setText(model.getMenuItems().get(position));
    }

    @Override
    public int getItemCount() {
        return model.getMenuItems().size();
    }

    public class LinearSet extends RecyclerView.ViewHolder {

        LinearLayout setLiner;
        protected TextView description;

        LinearSet(View v) {
            super(v);
            setLiner = v.findViewById(R.id.linear_item);
            description = v.findViewById(R.id.text_item_menu);

        }
    }
}
