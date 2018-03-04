package com.example.alex.fitofan.ui.fragments.participants;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterParticipants extends RecyclerView.Adapter<RecyclerAdapterParticipants.ViewHolder> {


    //Предоставляет ссылку на представления, используемые в RecyclerView

    private ParticipantFragment mParticipantFragment;
    private int count;

    public RecyclerAdapterParticipants(int count, ParticipantFragment mParticipantFragment) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_participants, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        CircleImageView imageUser = linear.findViewById(R.id.image_user_participant);

        Uri uri2 = Uri.parse("http://backbreaker.net/wp-content/uploads/2015/11/1295992106_brad_pitt.jpg");
        Glide.with(mParticipantFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                .load(uri2)
                .fitCenter()
                .thumbnail(0.5f)
                .priority(Priority.IMMEDIATE)
                .placeholder(R.mipmap.icon)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageUser); //ссылка на ImageView

    }

    @Override
    public int getItemCount() {
        return count;
    }

}
