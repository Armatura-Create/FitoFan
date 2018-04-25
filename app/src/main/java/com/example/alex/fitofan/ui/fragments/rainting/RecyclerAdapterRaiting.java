package com.example.alex.fitofan.ui.fragments.rainting;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class RecyclerAdapterRaiting extends RecyclerView.Adapter<RecyclerAdapterRaiting.ViewHolder> {


    //Предоставляет ссылку на представления, используемые в RecyclerView

    private ParticipantFragment mParticipantFragment;
    private int count;

    public RecyclerAdapterRaiting(int count, ParticipantFragment mParticipantFragment) {
        this.count = count;
        this.mParticipantFragment = mParticipantFragment;
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
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = null;
        switch (viewType) {
            case 0:
                linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_top_raiting, parent, false);
                break;
            case 1:
                linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_rating, parent, false);
                break;
        }
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        CircleImageView imageUser = linear.findViewById(R.id.image_user_raiting);
        TextView number = linear.findViewById(R.id.number_participant);

        if (position > 0) {

            Uri uri = Uri.parse("http://backbreaker.net/wp-content/uploads/2015/11/1295992106_brad_pitt.jpg");
            Glide.with(mParticipantFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                    .load(uri)
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .into(imageUser); //ссылка на ImageView
        }

        if (position > 0) {
            number.setText(position + 3 + ".");
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

}
