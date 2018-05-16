package com.example.alex.fitofan.ui.fragments.subscribers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.FragmentSubscribersBinding;
import com.example.alex.fitofan.interfaces.GetMyData;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.SubStatus;
import com.example.alex.fitofan.models.GetRatingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.user_profile.UserProfileActivity;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class SubscribersFragment extends Fragment implements ILoadingStatus<GetRatingModel>, SubStatus {

    private RecyclerAdapterSub adapter;
    private View view;
    private FragmentSubscribersBinding mBinding;
    private ArrayList<User> models;
    private Button sub;
    private int position;
    private String userId;

    @SuppressLint("ValidFragment")
    public SubscribersFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_subscribers, container, false);

        mBinding = DataBindingUtil.bind(view);
        initRequest();
        initRecyclerView();
        initListeners();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initRequest() {
        if (Connection.isNetworkAvailable(getContext())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("user_id", userId);
            Request.getInstance().getSubscribers(map, this);
        }
    }

    private void initListeners() {

    }

    public void sub(String uid, int position, Button sub) {
        this.position = position;
        this.sub = sub;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("user_id", uid);
        if (Connection.isNetworkAvailable(getContext())) {
            if (adapter.getmModel().get(position).getSubscribed() != 1)
                Request.getInstance().subscribeUser(map, this);
            if (adapter.getmModel().get(position).getSubscribed() == 1)
                Request.getInstance().unSubscribeUser(map, this);
        }
    }

    private void initRecyclerView() {
        models = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvSub.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterSub(models, this);
        mBinding.rvSub.setAdapter(adapter);
        mBinding.rvSub.setNestedScrollingEnabled(true);
    }

    @Override
    public void onSuccess(GetRatingModel info) {
        adapter.setmModel(info.getUsers());
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onSuccess(String info) {
        if (info.equals("true")) {
            sub.setText(getResources().getString(R.string.unsubscribe));
            adapter.getmModel().get(position).setSubscribed(1);
        }
        if (info.equals("false")) {
            sub.setText(getResources().getString(R.string.subscribe));
            adapter.getmModel().get(position).setSubscribed(0);
        }
        sub.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animation_scale_like));
    }

    @Override
    public void onFailure(String message) {

    }
}
