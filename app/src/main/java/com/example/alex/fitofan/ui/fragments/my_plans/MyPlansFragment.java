package com.example.alex.fitofan.ui.fragments.my_plans;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.FragmentMyPlansBinding;
import com.example.alex.fitofan.ui.activity.training.TrainingActivity;
import com.example.alex.fitofan.ui.fragments.wall.RecyclerAdapterWall;
import com.example.alex.fitofan.utils.ItemClickSupport;

public class MyPlansFragment extends Fragment {

    FragmentMyPlansBinding mBinding;
    private View view;
    private RecyclerAdapterMyPlans adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_plans, container, false);

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
        // обработка нажатий по элементу списка
        ItemClickSupport.addTo(mBinding.rvMyPlans).setOnItemClickListener((recyclerView, position, v) -> {
            startActivity(new Intent(view.getContext(), TrainingActivity.class));
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvMyPlans.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterMyPlans(5, this);
        mBinding.rvMyPlans.setAdapter(adapter);
        mBinding.rvMyPlans.setNestedScrollingEnabled(true);

    }
}
