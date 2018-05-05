package com.example.alex.fitofan.ui.activity.user_profile;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.FormatTime;
import com.google.gson.Gson;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.encodeQualityOf;
import static com.bumptech.glide.request.RequestOptions.placeholderOf;

public class RecyclerAdapterUserProfile extends RecyclerView.Adapter<RecyclerAdapterUserProfile.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private UserProfileActivity mUserProfileActivity;

    private User mUserModel;
    private ArrayList<GetTrainingModel> mWallModels;

    int numberOfClicks = 0;
    boolean threadStarted = false;
    final int DELAY_BETWEEN_CLICKS_IN_MILLISECONDS = 250;

    RecyclerAdapterUserProfile(UserProfileActivity mUserProfileActivity, User mUserModel, ArrayList<GetTrainingModel> mWallModels) {
        this.mWallModels = mWallModels;
        this.mUserProfileActivity = mUserProfileActivity;
        this.mUserModel = mUserModel;
    }

    public User getmUserModel() {
        return mUserModel;
    }

    public void setmUserModel(User mUserModel) {
        this.mUserModel = mUserModel;
    }

    public ArrayList<GetTrainingModel> getmWallModels() {
        return mWallModels;
    }

    public void setmWallModels(ArrayList<GetTrainingModel> mWallModels) {
        this.mWallModels = mWallModels;
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

        //header view

        //body view


        //header methods
        if (position == 0) {

            TextView firstName = linear.findViewById(R.id.first_name);
            TextView lastName = linear.findViewById(R.id.last_name);
            ImageView imageUser = linear.findViewById(R.id.user_photo);
            TextView city = linear.findViewById(R.id.city);
            TextView place = linear.findViewById(R.id.tv_place);
            TextView countPlans = linear.findViewById(R.id.tv_count_plans);
            TextView subscriberse = linear.findViewById(R.id.tv_subscriberse);

            if (mUserModel != null) {
                firstName.setText(mUserModel.getName());
                lastName.setText(mUserModel.getSurname());
                city.setText(mUserModel.getLocation());
                countPlans.setText(mUserModel.getTrainingPlans());
                place.setText(mUserModel.getRating());

                if (mUserModel.getImage_url() != null) {
                    Glide.with(mUserProfileActivity.getContext()) //передаем контекст приложения
                            .load(Uri.parse(mUserModel.getImage_url()))
                            .apply(centerCropTransform())
                            .transition(withCrossFade())
                            .apply(placeholderOf(R.drawable.background))
                            .apply(encodeQualityOf(10))
                            .into(imageUser); //ссылка на ImageView

                }

                linear.findViewById(R.id.show_all).setOnClickListener(view -> {
                    Toast.makeText(mUserProfileActivity.getContext(), "Show All", Toast.LENGTH_SHORT).show();
                });

                final RecyclerView recyclerView = linear.findViewById(R.id.rv_group);

                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(mUserProfileActivity.getApplicationContext(),
                                LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                final RecyclerAdapterInAdapter adapter = new RecyclerAdapterInAdapter(mUserProfileActivity);
                recyclerView.setAdapter(adapter);
            }
        }
        //body methods
        if (position >= 2) {
            ImageView imageTrainingPlan = linear.findViewById(R.id.image_training);
            TextView tvNameTranig = linear.findViewById(R.id.tv_training_name);
            TextView tvTotalTime = linear.findViewById(R.id.tv_total_time);
            TextView tvDescription = linear.findViewById(R.id.tv_description);

            LinearLayout planLeaner = linear.findViewById(R.id.plan_liner);

            if (mWallModels != null) {

                tvNameTranig.setText(mWallModels.get(position - 2).getName());
                tvTotalTime.setText(FormatTime.formatTime(Long.valueOf(mWallModels.get(position - 2).getPlan_time())));
                tvDescription.setText(mWallModels.get(position - 2).getDescription());

                Glide.with(mUserProfileActivity.getContext()) //передаем контекст приложения
                        .load(Uri.parse(mWallModels.get(position - 2).getImage()))
                        .apply(centerCropTransform())
                        .transition(withCrossFade())
                        .into(imageTrainingPlan);
            }

            planLeaner.setOnClickListener(v -> {
                ++numberOfClicks;
                if (!threadStarted) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            threadStarted = true;
                            try {
                                Thread.sleep(DELAY_BETWEEN_CLICKS_IN_MILLISECONDS);
                                if (numberOfClicks == 1) {
                                    mUserProfileActivity.goPreviewPlan(mWallModels.get(position - 2).getId());
                                } else if (numberOfClicks == 2) {
                                    if (mWallModels.get(position - 2).getLiked() != 1) {
                                        mUserProfileActivity.likePlan(mWallModels.get(position - 2).getId());
                                    }
                                }
                                numberOfClicks = 0;
                                threadStarted = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mWallModels == null ? 2 : mWallModels.size() + 2;
    }
}

