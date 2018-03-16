package com.example.alex.fitofan.ui.fragments.participants;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.FragmentParticipantsBinding;
import com.example.alex.fitofan.eventbus.MyPlansEvent;
import com.example.alex.fitofan.models.WallModel;
import com.example.alex.fitofan.ui.fragments.wall.RecyclerAdapterWall;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ParticipantFragment extends Fragment{

    private RecyclerAdapterParticipants adapter;
    private View view;
    private FragmentParticipantsBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_participants, container, false);

        mBinding = DataBindingUtil.bind(view);

        return view;
    }

    @Override
    public void onStart() {
        initRecyclerView();
        initListeners();
        super.onStart();
    }

    private void initListeners() {
        mBinding.leaderLiner.setOnClickListener(v -> {
            EventBus.getDefault().post(new MyPlansEvent());
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvParticipant.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterParticipants(30, this);
        mBinding.rvParticipant.setAdapter(adapter);
        mBinding.rvParticipant.setNestedScrollingEnabled(false);
    }
}
