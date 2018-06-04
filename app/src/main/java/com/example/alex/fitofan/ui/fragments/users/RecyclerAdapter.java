package com.example.alex.fitofan.ui.fragments.users;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.utils.ActionPlanCard;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements LikeStatus {


    //Предоставляет ссылку на представления, используемые в RecyclerView
    private final RequestOptions mRequestOptions;
    private UsersFragment mFragment;
    private ArrayList<User> usersModels;

    public RecyclerAdapter(ArrayList<User> usersModels, UsersFragment mFragment) {
        this.usersModels = usersModels;
        this.mFragment = mFragment;
        mRequestOptions = new RequestOptions();
    }

    public void setUsersModels(ArrayList<User> usersModels) {
        this.usersModels = usersModels;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //Определение класса ViewHolder
        private LinearLayout mLinearLayout;

        ViewHolder(LinearLayout v) {
            super(v);
            mLinearLayout = v;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(linear);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        CircleImageView imageUser = linear.findViewById(R.id.image_user);
        TextView name = linear.findViewById(R.id.name_user);
        TextView location = linear.findViewById(R.id.location_user);
        LinearLayout linearSub = linear.findViewById(R.id.linear_sub);

        imageUser.setBackgroundColor(Color.parseColor("#00000000"));

        if (usersModels.get(position).getImage_url() != null && !usersModels.get(position).getImage_url().equals("")) {
            Glide.with(mFragment.getContext()) //передаем контекст приложения
                    .load(Uri.parse(usersModels.get(position).getImage_url()))
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imageUser); //ссылка на ImageView
        }

        name.setText(usersModels.get(position).getName() + " " + usersModels.get(position).getSurname());
        location.setText(usersModels.get(position).getLocation());

        linearSub.setOnClickListener(view -> {
            ActionPlanCard.goUserProfile(mFragment.getContext(), usersModels.get(position).getUid());
        });
    }

    @Override
    public void onSuccess(String info) {

    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public int getItemCount() {
        return usersModels != null ? usersModels.size() : 0;
    }
}
