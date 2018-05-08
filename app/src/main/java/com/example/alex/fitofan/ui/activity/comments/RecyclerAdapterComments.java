package com.example.alex.fitofan.ui.activity.comments;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.CommentModel;
import com.example.alex.fitofan.models.ExerciseModelFromTraining;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.utils.FormatTime;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class RecyclerAdapterComments extends RecyclerView.Adapter<RecyclerAdapterComments.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private CommentsActivity trainingActivity;

    private ArrayList<CommentModel> model;

    public RecyclerAdapterComments(CommentsActivity trainingActivity, ArrayList<CommentModel> model) {
        this.trainingActivity = trainingActivity;
        this.model = model;
    }

    public void setModel(ArrayList<CommentModel> model) {
        this.model = model;
    }

    public ArrayList<CommentModel> getModel() {
        return model;
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
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        TextView comment = linear.findViewById(R.id.comment);
        ImageView image = linear.findViewById(R.id.image_user);

        comment.setText(model.get(position).getComment());

        if (model.get(position).getUser().getImage_url() != null) {
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(trainingActivity.getContext())
                    .load(Uri.parse(model.get(position).getUser().getImage_url()))
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .transition(withCrossFade())
                    .into(image);
        }
    }

    @Override
    public int getItemCount() {
        return model != null ? model.size() : 0;
    }
}
