package com.example.alex.fitofan.ui.fragments.rainting;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.utils.ActionPlanCard;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class RecyclerAdapterRaiting extends RecyclerView.Adapter<RecyclerAdapterRaiting.ViewHolder> {


    //Предоставляет ссылку на представления, используемые в RecyclerView

    private ParticipantFragment mParticipantFragment;
    private ArrayList<User> mModel;

    public ArrayList<User> getmModel() {
        return mModel;
    }

    public void setmModel(ArrayList<User> mModel) {
        this.mModel = mModel;
    }

    public RecyclerAdapterRaiting(ArrayList<User> mModel, ParticipantFragment mParticipantFragment) {
        this.mModel = mModel;
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
        CircleImageView topPhotoGold = linear.findViewById(R.id.user_photo_gold);
        CircleImageView topPhotoSilver = linear.findViewById(R.id.user_photo_silver);
        CircleImageView topPhotoBronze = linear.findViewById(R.id.user_photo_bronze);

        LinearLayout gold = linear.findViewById(R.id.linear_gold);
        LinearLayout silver = linear.findViewById(R.id.linear_silver);
        LinearLayout bronze = linear.findViewById(R.id.linear_bronze);
        LinearLayout rating = linear.findViewById(R.id.linear_rating);

        TextView number = linear.findViewById(R.id.number_participant);
        TextView like = linear.findViewById(R.id.like_rating);
        TextView like_gold = linear.findViewById(R.id.like_one);
        TextView like_silver = linear.findViewById(R.id.like_two);
        TextView like_bronze = linear.findViewById(R.id.like_three);
        TextView name = linear.findViewById(R.id.name_user_participant);
        TextView name_gold = linear.findViewById(R.id.name_one);
        TextView name_silver = linear.findViewById(R.id.name_two);
        TextView name_bronze = linear.findViewById(R.id.name_three);

        if (position == 0) {

            if (mModel.size() > 0) {
                Glide.with(mParticipantFragment.getActivity().getApplicationContext())
                        .load(Uri.parse(mModel.get(0).getImage_url()))
                        .apply(centerCropTransform())
                        .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .transition(withCrossFade())
                        .into(topPhotoGold);

                name_gold.setText(mModel.get(0).getName() + " " + mModel.get(0).getSurname());
                like_gold.setText(mModel.get(0).getLikes());
                gold.setOnClickListener(view -> {
                    ActionPlanCard.goUserProfile(mParticipantFragment.getContext(), mModel.get(0).getUid());
                });
            }

            if (mModel.size() > 1) {
                Glide.with(mParticipantFragment.getActivity().getApplicationContext())
                        .load(Uri.parse(mModel.get(1).getImage_url()))
                        .apply(centerCropTransform())
                        .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .transition(withCrossFade())
                        .into(topPhotoSilver);

                name_silver.setText(mModel.get(1).getName() + " " + mModel.get(1).getSurname());
                like_silver.setText(mModel.get(1).getLikes());
                silver.setOnClickListener(view -> {
                    ActionPlanCard.goUserProfile(mParticipantFragment.getContext(), mModel.get(1).getUid());
                });
            }
            if (mModel.size() > 2) {
                Glide.with(mParticipantFragment.getActivity().getApplicationContext())
                        .load(Uri.parse(mModel.get(2).getImage_url()))
                        .apply(centerCropTransform())
                        .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .transition(withCrossFade())
                        .into(topPhotoBronze);

                name_bronze.setText(mModel.get(2).getName() + " " + mModel.get(2).getSurname());
                like_bronze.setText(mModel.get(2).getLikes());
                bronze.setOnClickListener(view -> {
                    ActionPlanCard.goUserProfile(mParticipantFragment.getContext(), mModel.get(2).getUid());
                });
            }
        }

        if (position > 0) {

            Glide.with(mParticipantFragment.getActivity().getApplicationContext()) //передаем контекст приложения
                    .load(Uri.parse(mModel.get(position + 2).getImage_url()))
                    .apply(centerCropTransform())
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .transition(withCrossFade())
                    .into(imageUser); //ссылка на ImageView

            number.setText(position + 2 + "");
            name.setText(mModel.get(position + 2).getName() + " " + mModel.get(position + 2).getSurname());
            like.setText(mModel.get(position + 2).getLikes());

            rating.setOnClickListener(view -> {
                ActionPlanCard.goUserProfile(mParticipantFragment.getContext(), mModel.get(position + 2).getUid());
            });
        }
    }

    @Override
    public int getItemCount() {
        return mModel == null ? 0 : (mModel.size() > 3 ? mModel.size() - 2 : 1);
    }

}
