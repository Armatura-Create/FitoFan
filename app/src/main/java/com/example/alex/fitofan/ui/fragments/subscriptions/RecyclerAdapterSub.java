package com.example.alex.fitofan.ui.fragments.subscriptions;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class RecyclerAdapterSub extends RecyclerView.Adapter<RecyclerAdapterSub.ViewHolder> {


    //Предоставляет ссылку на представления, используемые в RecyclerView

    private SubscriptionsFragment mFragment;
    private ArrayList<User> mModel;

    public ArrayList<User> getmModel() {
        return mModel;
    }

    public void setmModel(ArrayList<User> mModel) {
        this.mModel = mModel;
    }

    public RecyclerAdapterSub(ArrayList<User> mModel, SubscriptionsFragment mFragment) {
        this.mModel = mModel;
        this.mFragment = mFragment;
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
                .inflate(R.layout.item_sub, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        CircleImageView imageUser = linear.findViewById(R.id.image_user);
        TextView name = linear.findViewById(R.id.name_user);
        TextView surname = linear.findViewById(R.id.surname_user);
        Button sub = linear.findViewById(R.id.bt_sub);

        LinearLayout linearSub = linear.findViewById(R.id.linear_sub);

        sub.setText(mFragment.getResources().getString(R.string.subscribe));
        sub.setVisibility(View.VISIBLE);

        if(mModel.get(position).getSubscribed() == 1){
            sub.setText(mFragment.getResources().getString(R.string.unsubscribe));
        }

        if(mModel.get(position).getUid()
                .equals(new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid())){
            sub.setVisibility(View.INVISIBLE);
        }

        if (mModel.get(position).getImage_url() != null) {
            Glide.with(mFragment.getContext()) //передаем контекст приложения
                    .load(Uri.parse(mModel.get(position).getImage_url()))
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .into(imageUser); //ссылка на ImageView
        }

        name.setText(mModel.get(position).getName());
        surname.setText(mModel.get(position).getSurname());

        sub.setOnClickListener(view -> {
            mFragment.sub(mModel.get(position).getUid(), position, sub);
        });

        linearSub.setOnClickListener(view -> {
            mFragment.goUserProfile(mModel.get(position).getUid());
        });

    }

    @Override
    public int getItemCount() {
        return mModel == null ? 0 : mModel.size();
    }

}
