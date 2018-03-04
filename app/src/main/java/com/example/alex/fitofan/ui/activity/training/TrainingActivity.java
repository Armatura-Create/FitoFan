package com.example.alex.fitofan.ui.activity.training;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityTrainingBinding;
import com.example.alex.fitofan.models.MapTrainingModel;
import com.example.alex.fitofan.models.TrainingModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrainingActivity extends AppCompatActivity implements TrainingContact.View, SoundPool.OnLoadCompleteListener {

    private ActivityTrainingBinding mBinding;

    private TrainingPresenter mPresenter;

    private TrainingModel mMapTrainingModel;

    private RecyclerAdapterTraining adapter;

    final public static long DEFAULT_REFRESH_INTERVAL = 1000L;

    private long mTimerStart = 5000L;
    private ScheduledExecutorService mTimerThread = null;
    private SoundPool sp;
    private int soundIdPoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_training);

        mPresenter = new TrainingPresenter(this);

        initListeners();
        initTraining();
        initSoundPoint();
        initRecyclerView();
    }

    private void initSoundPoint() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundIdPoint = sp.load(this, R.raw.point, 1);

    }

    private void initTraining() {
        mBinding.contentTraining.nameTraining.setText("Training");

        mMapTrainingModel = new TrainingModel();

        //Test
        ArrayList<MapTrainingModel> arr = new ArrayList<>();
        MapTrainingModel test1 = new MapTrainingModel();
        test1.setTimeExercise("15:00");
        test1.setCurrentExercise("Бег по кругу");
        arr.add(test1);
        MapTrainingModel test2 = new MapTrainingModel();
        test2.setTimeExercise("0:30");
        test2.setCurrentExercise("Отдых");
        arr.add(test2);
        MapTrainingModel test3 = new MapTrainingModel();
        test3.setTimeExercise("15:00");
        test3.setCurrentExercise("Бег по кругу");
        arr.add(test3);
        MapTrainingModel test4 = new MapTrainingModel();
        test4.setTimeExercise("0:30");
        test4.setCurrentExercise("Отдых");
        arr.add(test4);
        MapTrainingModel test5 = new MapTrainingModel();
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
//            Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
            if (mMapTrainingModel.getMapTrainingModels().size() == adapter.getMapPosition() + 1) {
                Toast.makeText(this, "Please restart", Toast.LENGTH_SHORT).show();
            } else if (!isRunning()) {
                startTimer();
            } else {
                Toast.makeText(this, "Already start", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.contentTraining.btStop.setOnClickListener(v -> {
            stopTimer();
        });
        mBinding.contentTraining.btReset.setOnClickListener(v -> {
            if (!isRunning()) {
                adapter.setMapPosition(0);
                adapter.notifyDataSetChanged();
                mTimerStart = 5000;
            } else {
                Toast.makeText(this, "Firstly stoped", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.contentTraining.tvPublish.setOnClickListener(v -> {
            Toast.makeText(this, "Опубликовать", Toast.LENGTH_SHORT).show();
        });
        mBinding.contentTraining.tvShare.setOnClickListener(v -> {
            Toast.makeText(this, "Поделиться", Toast.LENGTH_SHORT).show();
        });
    }

    private void startTimer() {

        long minute = ((mTimerStart / 1000) / 60L) % 60L,
                second = (mTimerStart / 1000) % 60L;
        mBinding.contentTraining.timer.setText(String.format("%02d:%02d", minute, second));

        mTimerThread = Executors.newSingleThreadScheduledExecutor();
        mTimerThread.scheduleWithFixedDelay(() -> new Handler(Looper.getMainLooper()).post(() -> {
            int elapsedTime = (int) ((mTimerStart - DEFAULT_REFRESH_INTERVAL) / 1000);
            if (elapsedTime <= 0) {
                if (mMapTrainingModel.getMapTrainingModels().size() > adapter.getMapPosition() + 1) {
                    adapter.setMapPosition(adapter.getMapPosition() + 1);
                    adapter.notifyDataSetChanged();
                    //+2 sec
                    mTimerStart = 7000L;
                    stopTimer();
                    sp.play(soundIdPoint, 1, 1, 0, 0, 1);
                    startTimer();
                } else {
                    Toast.makeText(this, "Complete", Toast.LENGTH_SHORT).show();
                    stopTimer();
                }
            }
            mTimerStart -= DEFAULT_REFRESH_INTERVAL;
            long minutes = (elapsedTime / 60L) % 60L,
                    seconds = elapsedTime % 60L;

            mBinding.contentTraining.timer.setText(String.format("%02d:%02d", minutes, seconds));
        }), DEFAULT_REFRESH_INTERVAL, DEFAULT_REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public synchronized void stopTimer() {
        if (mTimerThread == null) return;

        mTimerThread.shutdown();
        mTimerThread = null;
    }

    public synchronized boolean isRunning() {
        return (mTimerThread != null);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
