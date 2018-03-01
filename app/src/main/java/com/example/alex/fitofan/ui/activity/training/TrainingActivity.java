package com.example.alex.fitofan.ui.activity.training;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivitySettingsBinding;
import com.example.alex.fitofan.databinding.ActivityTrainingBinding;
import com.example.alex.fitofan.models.MapTrainingModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class TrainingActivity extends AppCompatActivity implements TrainingContact.View {

    private ActivityTrainingBinding mBinding;

    private TrainingPresenter presenter;

    private TrainingModel mMapTrainingModel;

    private RecyclerAdapterTraining adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_training);

        presenter = new TrainingPresenter(this);

        initTraining();
        initListeners();
        initRecyclerView();
    }

    private void initTraining() {
        mBinding.contentTraining.nameTraining.setText("Training");

        mMapTrainingModel = new TrainingModel();

        //Test
        ArrayList<MapTrainingModel> arr = new ArrayList<>();
        MapTrainingModel test1= new MapTrainingModel();
        test1.setTimeExercise("15:00");
        test1.setCurrentExercise("Бег по кругу");
        arr.add(test1);
        MapTrainingModel test2= new MapTrainingModel();
        test2.setTimeExercise("0:30");
        test2.setCurrentExercise("Отдых");
        arr.add(test2);
        MapTrainingModel test3= new MapTrainingModel();
        test3.setTimeExercise("15:00");
        test3.setCurrentExercise("Бег по кругу");
        arr.add(test3);
        MapTrainingModel test4= new MapTrainingModel();
        test4.setTimeExercise("0:30");
        test4.setCurrentExercise("Отдых");
        arr.add(test4);
        MapTrainingModel test5= new MapTrainingModel();
        test5.setTimeExercise("15:00");
        test5.setCurrentExercise("Бег по кругу");
        arr.add(test5);

        mMapTrainingModel.setMapTrainingModels(arr);
    }

    private void initRecyclerView() {

        mBinding.contentTraining.mapTraining.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.contentTraining.mapTraining.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterTraining(mMapTrainingModel.getMapTrainingModels(), this);
        mBinding.contentTraining.mapTraining.setAdapter(adapter);

    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
        mBinding.contentTraining.btPlay.setOnClickListener(v -> {
            Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
            if(mMapTrainingModel.getMapTrainingModels().size() > adapter.getMapPosition()) {
                adapter.setMapPosition(adapter.getMapPosition() + 1);
            } else {
                adapter.setMapPosition(0);
            }
            adapter.notifyDataSetChanged();
        });
        mBinding.contentTraining.btStop.setOnClickListener(v -> {
            Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
        });
        mBinding.contentTraining.btReset.setOnClickListener(v -> {
            Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
        });
        mBinding.contentTraining.tvPublish.setOnClickListener(v -> {
            Toast.makeText(this, "Опубликовать", Toast.LENGTH_SHORT).show();
        });
        mBinding.contentTraining.tvShare.setOnClickListener(v -> {
            Toast.makeText(this, "Поделиться", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public Context getContext() {
        return this;
    }


}
