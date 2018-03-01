package com.example.alex.fitofan.ui.fragments.my_plans;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.FragmentMyPlansBinding;
import com.example.alex.fitofan.ui.activity.training.TrainingActivity;

public class MyPlansFragment extends Fragment {

    FragmentMyPlansBinding mBinding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_plans, container, false);

        mBinding = DataBindingUtil.bind(view);

        mBinding.goTraining.setOnClickListener(v -> {
            Log.e("onCreateView: ", "go");
            startActivity(new Intent(view.getContext(), TrainingActivity.class));
        });

        return view;
    }
}
