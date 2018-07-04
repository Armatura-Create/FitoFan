package com.example.alex.fitofan.ui.fragments.users;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.FragmentUsersBinding;
import com.example.alex.fitofan.interfaces.SearchStatus;
import com.example.alex.fitofan.models.GetSearchPlansModel;
import com.example.alex.fitofan.models.GetSearchUsersModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.Connection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UsersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchStatus {

    private FragmentUsersBinding mBinding;
    private View view;
    private RecyclerAdapter adapter;
    private ArrayList<User> users;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_users, container, false);
        mBinding = DataBindingUtil.bind(view);

        users = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        initListeners();
        initRecyclerView();
        search("");
        super.onStart();
    }

    private void initListeners() {
        mBinding.refresh.setOnRefreshListener(this);

        mBinding.itemSearch.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search(mBinding.itemSearch.search.getText().toString());
            }
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext());
        mBinding.rv.setItemViewCacheSize(100);
        mBinding.rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(users, this);
        mBinding.rv.setAdapter(adapter);
        mBinding.rv.setNestedScrollingEnabled(true);
    }

    private void search(String s) {
        if (Connection.isNetworkAvailable(getContext())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("search", s);
            Request.getInstance().searchUsers(map, this);
        } else {
            mBinding.refresh.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        search("");
        mBinding.refresh.setRefreshing(true);
    }

    @Override
    public void onSuccessPlans(GetSearchPlansModel info) {

    }

    @Override
    public void onSuccessUsers(GetSearchUsersModel info) {
        if (info.getUsers() != null && info.getUsers().size() > 0) {
            users.clear();
            users.addAll(info.getUsers());
            adapter.setUsersModels(users);
            adapter.notifyDataSetChanged();
        }
        mBinding.refresh.setRefreshing(false);
    }

    @Override
    public void onFailure(String message) {
        mBinding.refresh.setRefreshing(false);
    }
}
