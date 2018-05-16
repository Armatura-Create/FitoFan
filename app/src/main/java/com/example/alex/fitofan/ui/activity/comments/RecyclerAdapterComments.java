package com.example.alex.fitofan.ui.activity.comments;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.CommentModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.ActionPlanCard;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class RecyclerAdapterComments extends RecyclerView.Adapter<RecyclerAdapterComments.LinearSet> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private CommentsActivity mCommentsActivity;

    private ArrayList<CommentModel> model;
    private SparseBooleanArray mSelectedPositions;
    private String userIdTraning;
    private int fullCount;
    private LinearSet holder;

    public RecyclerAdapterComments(CommentsActivity mCommentsActivity, ArrayList<CommentModel> model, String userIdTraning) {
        this.mCommentsActivity = mCommentsActivity;
        this.model = model;
        this.userIdTraning = userIdTraning;
    }

    public void setModel(ArrayList<CommentModel> model) {
        this.model = model;
        mSelectedPositions = new SparseBooleanArray(model.size());
    }

    public SparseBooleanArray getSelectedPositions() {
        return mSelectedPositions;
    }

    public void setSelectedPositions(SparseBooleanArray mSelectedPositions) {
        this.mSelectedPositions = mSelectedPositions;
    }

    public ArrayList<CommentModel> getModel() {
        return model;
    }

    @Override
    public LinearSet onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new LinearSet(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull LinearSet holder, final int position) {
        //Заполнение заданного представления данными

        this.holder = holder;

        holder.comment.setText(model.get(position).getComment());
        holder.timeCreation.setText(model.get(position).getCreateTime());

        if (model.get(position).getUser().getImage_url() != null) {
            holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mCommentsActivity.getContext())
                    .load(Uri.parse(model.get(position).getUser().getImage_url()))
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .transition(withCrossFade())
                    .into(holder.image);
        }

        holder.setItem(mSelectedPositions.get(position));
    }

    @Override
    public int getItemCount() {
        return model != null ? model.size() : 0;
    }

    public LinearSet getHolder() {
        return holder;
    }

    public void setHolder(LinearSet holder) {
        this.holder = holder;
    }

    public class LinearSet extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected LinearLayout setLiner;
        protected TextView comment;
        protected TextView timeCreation;
        protected ImageView image;

        public LinearSet(View v) {
            super(v);
            setLiner = v.findViewById(R.id.linear_comment);
            comment = v.findViewById(R.id.comment);
            image = v.findViewById(R.id.image_user);
            timeCreation = v.findViewById(R.id.time_creation);

            setLiner.setOnClickListener(this);
            image.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(setLiner)) {
                if (model.get(getAdapterPosition()).getUser().getUid().equals(
                        new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
                ) || userIdTraning.equals(
                        new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
                )) {
                    changeState(getAdapterPosition());
                }
            }
            if (v.equals(image)) {
                ActionPlanCard.goUserProfile(mCommentsActivity.getContext(), model.get(getAdapterPosition()).getUser().getUid());
            }
        }

        public void changeState(int position) {
            String[] ids = new String[mSelectedPositions.size()];

            boolean state;
            state = mSelectedPositions.get(position);
            state = !state;
            mSelectedPositions.put(position, state);
            for (int j = 0; j < mSelectedPositions.size(); j++) {
                if (mSelectedPositions.get(j)) {
                    ids[j] = model.get(j).getId();
                }
            }
            mCommentsActivity.setActionBar(mSelectedPositions, ids);
            notifyItemChanged(position);
        }

        public void setItem(boolean station) {
            if (station) {
                setLiner.setBackgroundResource(R.color.background_delimiter);
            } else {
                setLiner.setBackgroundResource(R.color.transparent);
            }
            mSelectedPositions.put(getAdapterPosition(), station);
        }

        public void removeAt() {
            for (int i = mSelectedPositions.size() - 1; i >= 0 ; i--) {
                if (mSelectedPositions.get(i)) {
                    model.remove(i);
                    mSelectedPositions.delete(i);
                    notifyItemRemoved(i);

                    for (int j = i; j < getItemCount(); j++) {
                        mSelectedPositions.put(j, mSelectedPositions.get(j + 1));

                    }
                    notifyItemRangeChanged(getAdapterPosition(), model.size());
                }
            }

        }
    }
}
