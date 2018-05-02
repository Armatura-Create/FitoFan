package com.example.alex.fitofan.ui.fragments.wall;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.FragmentWallBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.GetWallModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.models.WallModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.ui.activity.user_profile.UserProfileActivity;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class WallFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ILoadingStatus<GetWallModel>,LikeStatus {

    FragmentWallBinding mBinding;
    private View view;
    private RecyclerAdapterWall adapter;
    private int scrolling;
    private ArrayList<GetTrainingModel> models;

    private boolean isLoading;
    private boolean isRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wall, container, false);

        mBinding = DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onStart() {
        models = new ArrayList<>();
        initListeners();
        initRecyclerView();
        startRequest();
        super.onStart();
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

    protected void goUserProfile(String uid) {
        Intent intent = new Intent(getContext(), UserProfileActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    protected void goPreviewPlan(String planId) {
        Intent intent = new Intent(getContext(), PreviewPlanActivity.class);
        intent.putExtra("planId", planId);
        intent.putExtra("isWall", true);
        startActivity(intent);
    }

    protected void likePlan(String id){
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        Request.getInstance().like(map, this);
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
            models.addAll(info.getTraining());
            adapter.setmWallModels(models);
            adapter.notifyDataSetChanged();
            scrolling++;
            isLoading = false;
            isRefresh = false;
            scrolling++;
        } else if (info.getTraining() != null) {
            models.addAll(info.getTraining());
            adapter.setmWallModels(models);
            adapter.notifyDataSetChanged();
            scrolling++;
            isLoading = false;
        }
        mBinding.refresh.setRefreshing(false);
    }

    @Override
    public void onSuccess(Boolean info) {
        Toast.makeText(getContext(), info + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String message) {
        mBinding.refresh.setRefreshing(false);
    }
}
