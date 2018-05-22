package com.example.alex.fitofan.utils.CustomDialog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.TagModelCard;

public class RecyclerAdapterCard extends RecyclerView.Adapter<RecyclerAdapterCard.LinearSet> {

    //Предоставляет ссылку на представления, используемые в RecyclerView

    private TagModelCard model;
    private SparseBooleanArray mSelectedPositions;
    private LinearSet holder;

    public RecyclerAdapterCard() {
        model = new TagModelCard();
        mSelectedPositions = new SparseBooleanArray(model.getTag().size());
        mSelectedPositions.put(0, true);
    }

    @Override
    public LinearSet onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_rv, parent, false);
        return new LinearSet(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull LinearSet holder, final int position) {
        //Заполнение заданного представления данными

        this.holder = holder;

        holder.tag.setText(model.getTag().get(position));

        holder.setItem(mSelectedPositions.get(position));

    }

    @Override
    public int getItemCount() {
        return model != null ? model.getTag().size() : 0;
    }

    public LinearSet getHolder() {
        return holder;
    }

    public void setHolder(LinearSet holder) {
        this.holder = holder;
    }

    public class LinearSet extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected LinearLayout setLiner;
        protected TextView tag;

        public LinearSet(View v) {
            super(v);
            setLiner = v.findViewById(R.id.linear);
            tag = v.findViewById(R.id.type);

            setLiner.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(setLiner)) {
                changeState(getAdapterPosition());
            }
        }

        public void changeState(int position) {
            for (int j = 0; j < mSelectedPositions.size(); j++) {
                mSelectedPositions.put(j, false);
            }
            mSelectedPositions.put(position, true);

            notifyDataSetChanged();
        }

        public void setItem(boolean station) {
            if (station) {
                setLiner.setBackgroundResource(R.drawable.radius_liner_gray);
            } else {
                setLiner.setBackgroundResource(R.color.transparent);
            }
            mSelectedPositions.put(getAdapterPosition(), station);
        }
    }
}
