package com.example.alex.fitofan.ui.fragments.rainting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.FragmentRaintingBinding;

public class ParticipantFragment extends Fragment{

    private RecyclerAdapterRaiting adapter;
    private View view;
    private FragmentRaintingBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rainting, container, false);

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
//
//        final int[] CookingTimeHour = new int[1];
//        final int[] CookingTimeMin = new int[1];
//        final int[] CookingTimeSec = new int[1];


//        mBinding.leaderLiner.setOnClickListener(v -> {
//            MyTimePickerDialog tpd = new MyTimePickerDialog(getContext(), new MyTimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(com.ikovac.timepickerwithseconds.TimePicker view, int hourOfDay, int minute, int seconds) {
//                    CookingTimeHour[0] = hourOfDay;
//                    CookingTimeMin[0] = minute;
//                    CookingTimeSec[0] = minute;
//                }
//            }, CookingTimeHour[0], CookingTimeMin[0], CookingTimeSec[0], true);
//            tpd.show();
//        });
        mBinding.allRating.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "all rating", Toast.LENGTH_SHORT).show();
        });

        mBinding.friendRating.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "friend rating", Toast.LENGTH_SHORT).show();
        });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvRaiting.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterRaiting(30, this);
        mBinding.rvRaiting.setAdapter(adapter);
        mBinding.rvRaiting.setNestedScrollingEnabled(true);
    }
}
