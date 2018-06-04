package com.example.alex.fitofan.ui.fragments.rainting;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.FragmentRaintingBinding;
import com.example.alex.fitofan.interfaces.GetMyData;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.models.GetRatingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class ParticipantFragment extends Fragment implements ILoadingStatus<GetRatingModel>, SwipeRefreshLayout.OnRefreshListener, GetMyData {

    private RecyclerAdapterRaiting adapter;
    private View view;
    private FragmentRaintingBinding mBinding;
    private int scrolling;
    private ArrayList<User> models;

    private boolean isLoading;
    private boolean isRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rainting, container, false);

        mBinding = DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onStart() {
        initRequest();
        initRecyclerView();
        initListeners();
        initYourPlace();
        super.onStart();
    }

    private void initRequest() {
        if (Connection.isNetworkAvailable(getContext())) {
            scrolling = 1;
            mBinding.refresh.setRefreshing(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("page", String.valueOf(scrolling));
            isRefresh = true;
            Request.getInstance().getRating(map, this);
        } else {
            mBinding.refresh.setRefreshing(false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initYourPlace() {
        User user = new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser();
        mBinding.yourPlace.nameUserParticipant.setText(
                user.getName() + " " + user.getSurname());

        mBinding.yourPlace.likeRating.setText(user.getLikes());

        mBinding.yourPlace.numberParticipant.setText(user.getRating());

        if (user.getImage_url() != null && !user.getImage_url().equals("")) {
            Glide.with(getContext())
                    .load(Uri.parse(user.getImage_url()))
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .into(mBinding.yourPlace.imageUserRaiting);
        }
    }

    private void goNexLoad() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("page", String.valueOf(scrolling));
        Request.getInstance().getRating(map, this);
    }

    private void initListeners() {
        mBinding.refresh.setOnRefreshListener(this);

        mBinding.allRating.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "all rating", Toast.LENGTH_SHORT).show();
        });

        mBinding.friendRating.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "friend rating", Toast.LENGTH_SHORT).show();
        });

    }

    private void initRecyclerView() {
        models = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvRaiting.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterRaiting(models, this);
        mBinding.rvRaiting.setAdapter(adapter);
        mBinding.rvRaiting.setNestedScrollingEnabled(true);

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
            }
        };
        mBinding.rvRaiting.setOnScrollListener(scrollListener);
    }

    @Override
    public void onSuccess(GetRatingModel info) {
        if (isRefresh) {
            models.clear();
            models.addAll(info.getUsers());
            adapter.setmModel(models);
            adapter.notifyDataSetChanged();
            scrolling++;
            isLoading = false;
            isRefresh = false;
            scrolling++;
        } else if (info.getUsers() != null && info.getUsers().size() > 15) {
            models.addAll(info.getUsers());
            adapter.setmModel(models);
            adapter.notifyDataSetChanged();
            scrolling++;
            isLoading = false;
        }
        mBinding.refresh.setRefreshing(false);
    }

    @Override
    public void onSuccess(User info) {
        mBinding.yourPlace.likeRating.setText(info.getLikes());
        GetUserModel app = new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class);
        app.getUser().setLikes(info.getLikes());

        MSharedPreferences.getInstance().setUserInfo(new Gson().toJson(app));
    }

    @Override
    public void onFailure(String message) {
        mBinding.refresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        isLoading = true;
        mBinding.refresh.setRefreshing(false);
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("page", "1");
        scrolling = 1;
        Request.getInstance().getRating(map, this);
        Request.getInstance().getMyData(this);
    }
}
