package com.example.alex.fitofan.ui.activity.training;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.adgvcxz.cardlayoutmanager.CardLayoutManager;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityTrainingBinding;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.UnpackingTraining;
import com.example.alex.fitofan.utils.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrainingActivity extends AppCompatActivity implements TrainingContact.View, SoundPool.OnLoadCompleteListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private ActivityTrainingBinding mBinding;
    private TrainingPresenter mPresenter;
    private TrainingModel mTrainingModel;
    private Dao<TrainingModel, Integer> mTrainings;
    private MediaPlayer mMediaPlayer;

    private static final long DEFAULT_REFRESH_INTERVAL = 1000L;

    private ScheduledExecutorService mTimerThread = null;
    private SoundPool sp;
    private int soundIdPoint;
    private CardLayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private int mPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_training);

        mPresenter = new TrainingPresenter(this);

        initListeners();
        initTraining(getIntent().getIntExtra("trainingModel", -1));
        initSoundPoint();
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void initSoundPoint() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundIdPoint = sp.load(this, R.raw.point, 1);

    }

    //    void moveExercise(int i){
//        adapter.setMapPosition(adapter.getMapPosition() + i);
//        mBinding.content.tvDescriptionExercise.setText(
//                mTrainingModel.getExercises().get(adapter.getMapPosition()).getDescription()
//        );
//        setImageExercise(mTrainingModel.getExercises().get(adapter.getMapPosition()).getImage());
//        adapter.notifyDataSetChanged();
//    }
//
    private void startMusicExercise(String audioUri) {
        if (audioUri != null) {
            Log.e("startMusicExercise: ", audioUri);
            Uri bufUri = Uri.parse(audioUri);
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(this, bufUri);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("startMusicExercise: ", "beda");
            Toast.makeText(getContext(), "No audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void initTraining(int trainingModel) {
        if (trainingModel >= 0) {
            try {
                mTrainings = OpenHelperManager.getHelper(this, DatabaseHelper.class).getTrainingDAO();
                assert mTrainings != null;
                mTrainingModel = mTrainings.queryForId(trainingModel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            mTrainingModel = new TrainingModel();
        }
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(this, UnpackingTraining.buildExercises(mTrainingModel));
        mBinding.content.rv.setAdapter(adapter);
    }

    private void initListeners() {

        mBinding.content.btStart.setOnClickListener(v -> {
            if (!isRunning()) {
                Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
                mBinding.content.btStart.setText("STOP");
                layoutManager.setHorizontalSwipe(false);
                preparationTimer();
            } else {
                Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
                mBinding.content.btStart.setText("START");
                layoutManager.setHorizontalSwipe(true);
                stopTimer();
            }
        });
    }

    private void preparationTimer() {
        final long[] time = {5000L};
//        setTime(time[0]);
        layoutManager.setHorizontalSwipe(false);
        mTimerThread = Executors.newSingleThreadScheduledExecutor();
        mTimerThread.scheduleWithFixedDelay(() -> new Handler(Looper.getMainLooper()).post(() -> {
            long elapsedTime = time[0] - DEFAULT_REFRESH_INTERVAL;
            if (elapsedTime <= 0) {
                stopTimer();
                sp.play(soundIdPoint, 1, 1, 0, 0, 1);
                startTimer(mTrainingModel.getExercises().get(layoutManager.getTopPosition()).getTime());
            }
            time[0] -= DEFAULT_REFRESH_INTERVAL;
//            setTime(elapsedTime);
        }), DEFAULT_REFRESH_INTERVAL, DEFAULT_REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void startTimer(long timerStart) {
        layoutManager.setHorizontalSwipe(false);
        final long[] tempTime = {timerStart};
//        setTime(timerStart);
        if (mMediaPlayer != null) {
            releaseMP();
        }
        Log.e("position: ", String.valueOf(layoutManager.getTopPosition()));
        startMusicExercise(mTrainingModel.getExercises().get(layoutManager.getTopPosition()).getAudio());
        mTimerThread = Executors.newSingleThreadScheduledExecutor();
        mTimerThread.scheduleWithFixedDelay(() -> new Handler(Looper.getMainLooper()).post(() -> {
            long elapsedTime = tempTime[0] - DEFAULT_REFRESH_INTERVAL;
            if (elapsedTime <= 0) {
                stopTimer();
                if (layoutManager.getTopPosition() < adapter.getItemCount() - 1) {
                    mBinding.content.rv.smoothScrollToPosition(0);
                    startTimer(mTrainingModel.getExercises().get(layoutManager.getTopPosition() + 1).getTime());
                } else {
                    Toast.makeText(getContext(), "Complite", Toast.LENGTH_SHORT).show();
                    mBinding.content.btStart.setText("START");
                }
                sp.play(soundIdPoint, 1, 1, 0, 0, 1);
            }
            tempTime[0] -= DEFAULT_REFRESH_INTERVAL;
//            setTime(elapsedTime);
        }), DEFAULT_REFRESH_INTERVAL, DEFAULT_REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
    }

//    private void setTime(long timerStart) {
//        adapter.setTime(timerStart, layoutManager.getTopPosition());
//        adapter.notifyItemChanged(layoutManager.getTopPosition());
//    }

    private void releaseMP() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public Context getContext() {
        return this;
    }

    public synchronized void stopTimer() {
        layoutManager.setHorizontalSwipe(true);
        if (mTimerThread == null) return;

        releaseMP();
        mTimerThread.shutdown();
        mTimerThread = null;
    }

    public synchronized boolean isRunning() {
        return (mTimerThread != null);
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
        stopTimer();
    }
}
