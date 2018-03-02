package com.example.alex.fitofan.ui.fragments.wall;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.FragmentWallBinding;

public class WallFragment extends Fragment {

    FragmentWallBinding mBinding;
    private View view;
    private RecyclerAdapterWall adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wall, container, false);

        mBinding = DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onStart() {
        initListeners();
        initRecyclerView();
        super.onStart();
    }

    private void initListeners() {

    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvWall.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterWall(5, this);
        mBinding.rvWall.setAdapter(adapter);
        mBinding.rvWall.setNestedScrollingEnabled(true);

    }
}
