package com.example.alex.fitofan.ui.fragments.wall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.FragmentWallBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.interfaces.SaveStatus;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.GetWallModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.comments.CommentsActivity;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.ui.activity.user_profile.UserProfileActivity;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class WallFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ILoadingStatus<GetWallModel>, LikeStatus, SaveStatus {

    FragmentWallBinding mBinding;
    private View view;
    private RecyclerAdapterWall adapter;
    private int scrolling;
    private ArrayList<GetTrainingModel> models;

    private boolean isLoading;
    private boolean isRefresh;
    private ImageView like;
    private int position;
    private ImageView save;
    private TextView countLike;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wall, container, false);

        mBinding = DataBindingUtil.bind(view);
        models = new ArrayList<>();
        initListeners();
        initRecyclerView();
        startRequest();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void startRequest() {
        if (Connection.isNetworkAvailable(getContext())) {
            scrolling = 1;
            mBinding.refresh.setRefreshing(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("page", String.valueOf(scrolling));
            Request.getInstance().getWall(map, this);
        }
    }

    private void initListeners() {
        mBinding.refresh.setOnRefreshListener(this);

        mBinding.fabAddTraining.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), CreatePlanActivity.class));
        });
    }

    protected void likePlan(String id, ImageView like, TextView countLike, boolean isButton, int position) {
        this.countLike = countLike;
        this.like = like;
        this.position = position;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        if (Connection.isNetworkAvailable(getContext())) {
            if (!isButton) {
                Request.getInstance().like(map, this);
            }
            if (isButton) {
                if (models.get(position).getLiked() == 1)
                    Request.getInstance().dislikePlan(map, this);
                if (models.get(position).getLiked() != 1)
                    Request.getInstance().like(map, this);
            }
        }
    }

    protected void savePlan(String id, ImageView save, int position) {
        this.save = save;
        this.position = position;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        if (Connection.isNetworkAvailable(getContext())) {
            if (models.get(position).getIsSaved() == 1)
                Request.getInstance().unSavePlan(map, this);
            if (models.get(position).getIsSaved() != 1)
                Request.getInstance().savePlan(map, this);
        }
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvWall.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterWall(models, this);
        mBinding.rvWall.setAdapter(adapter);
        mBinding.rvWall.setNestedScrollingEnabled(true);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();//смотрим сколько элементов на экране
                int totalItemCount = linearLayoutManager.getItemCount();//сколько всего элементов
                int firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента

                if (!isLoading) {//проверяем, грузим мы что-то или нет, эта переменная должна быть вне класса  OnScrollListener
                    if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                        isLoading = true;//ставим флаг что мы попросили еще элемены
                        goNexLoad();
                    }
                }

                if (dy > 0 || dy < 0 && mBinding.fabAddTraining.isShown())
                    mBinding.fabAddTraining.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mBinding.fabAddTraining.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        mBinding.rvWall.setOnScrollListener(scrollListener);
    }

    private void goNexLoad() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("page", String.valueOf(scrolling));
        Request.getInstance().getWall(map, this);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        isRefresh = true;
        isLoading = true;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("page", "1");
        scrolling = 1;
        Request.getInstance().getWall(map, this);
    }

    @Override
    public void onSuccess(GetWallModel info) {
        if (isRefresh) {
            models.clear();
            models.addAll(info.getTrainings());
            adapter.setmWallModels(models);
            adapter.notifyDataSetChanged();
            scrolling++;
            isLoading = false;
            isRefresh = false;
            scrolling++;
        } else if (info.getTrainings() != null) {
            models.addAll(info.getTrainings());
            adapter.setmWallModels(models);
            adapter.notifyDataSetChanged();
            scrolling++;
            isLoading = false;
        }
        mBinding.refresh.setRefreshing(false);
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    public void onSuccess(Boolean info) {
        if (info) {
            if (models.get(position).getLiked() != 1) {
                like.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_full_red));
                countLike.setText(getResources().getString(R.string.like) + ": " +
                        String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) + 1)
                );
                adapter.getmWallModels().get(position).setLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) + 1));
                models.get(position).setLiked(1);
            }
        }
        if (!info) {
            if (models.get(position).getLiked() == 1) {
                like.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
                countLike.setText(getResources().getString(R.string.like) + ": " +
                        String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) - 1)
                );
                adapter.getmWallModels().get(position).setLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) - 1));
                models.get(position).setLiked(0);
            }
        }
        like.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animation_scale_like));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onSuccess(int status) {
        if (status == 1) {
            save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_full_black));
            models.get(position).setIsSaved(1);
        }
        if (status != 1) {
            save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_black));
            models.get(position).setIsSaved(0);
        }
        save.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animation_scale_like));
    }

    @Override
    public void onFailure(String message) {
        mBinding.refresh.setRefreshing(false);
    }
}
