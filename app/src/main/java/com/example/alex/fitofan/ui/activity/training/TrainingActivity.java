package com.example.alex.fitofan.ui.activity.training;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityTrainingBinding;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.ui.activity.main.MainActivity;
import com.example.alex.fitofan.utils.FormatTime;
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
    private RecyclerAdapterTraining adapter;
    private Dao<TrainingModel, Integer> mTrainings;
    private GestureDetectorCompat mSwipeDetector;
    private MediaPlayer mMediaPlayer;

    private static final long DEFAULT_REFRESH_INTERVAL = 1000L;
    private static final int SWIPE_MIN_DISTANCE = 130;
    private static final int SWIPE_MAX_DISTANCE = 300;
    private static final int SWIPE_MIN_VELOCITY = 200;

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
        initTraining(getIntent().getIntExtra("trainingModel", -1));
        initSoundPoint();
        initRecyclerView();
    }

    protected class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_DISTANCE)
                return false;
            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MIN_VELOCITY) {
                if (adapter.getMapPosition() > 0) {
                    moveExeercise(1);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.first_exercise), Toast.LENGTH_SHORT).show();
                }
            } else if (e2.getX() + e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MIN_VELOCITY) {
                if (adapter.getMapPosition() < mTrainingModel.getExercises().size() - 1) {
                    moveExeercise(-1);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.last_exercise), Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }

    }

    private void initSoundPoint() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundIdPoint = sp.load(this, R.raw.point, 1);

    }

    void moveExeercise(int i){
        adapter.setMapPosition(adapter.getMapPosition() + i);
        mBinding.contentTraining.tvDescriptionExercise.setText(
                mTrainingModel.getExercises().get(adapter.getMapPosition()).getDescription()
        );
        setImageExercise(mTrainingModel.getExercises().get(adapter.getMapPosition()).getImage());
        adapter.notifyDataSetChanged();
    }

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
    }

    private void initRecyclerView() {

        mBinding.contentTraining.mapTraining.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.contentTraining.mapTraining.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterTraining(mTrainingModel.getExercises(), this);
        mBinding.contentTraining.mapTraining.setAdapter(adapter);

        mBinding.contentTraining.tvDescriptionExercise.setText(
                mTrainingModel.getExercises().get(adapter.getMapPosition()).getDescription()
        );
        setImageExercise(mTrainingModel.getExercises().get(adapter.getMapPosition()).getImage());

    }

    private void initListeners() {

        mSwipeDetector = new GestureDetectorCompat(this, new MyGestureListener());
        mBinding.contentTraining.swipeTraining.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mSwipeDetector.onTouchEvent(event);
            }
        });

        mBinding.contentTraining.btPlay.setOnClickListener(v -> {
//            Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
            if (mTrainingModel.getExercises().size() == adapter.getMapPosition()) {
                Toast.makeText(this, "Please restart", Toast.LENGTH_SHORT).show();
            } else if (!isRunning()) {
                startTimer(mTrainingModel.getExercises().get(adapter.getMapPosition()).getTime());
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
            } else {
                Toast.makeText(this, "Firstly stoped", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.tvClose.setOnClickListener(v -> {
            mPresenter.close();
        });
        mBinding.tvHide.setOnClickListener(v -> {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        });
    }

    private void startTimer(long timerStart) {
        final long[] tempTime = {timerStart};
        mBinding.contentTraining.timer.setText(FormatTime.formatTime(tempTime[0]));
        startMusicExercise(mTrainingModel.getExercises().get(adapter.getMapPosition()).getAudio());
        mTimerThread = Executors.newSingleThreadScheduledExecutor();
        mTimerThread.scheduleWithFixedDelay(() -> new Handler(Looper.getMainLooper()).post(() -> {
            long elapsedTime = tempTime[0] - DEFAULT_REFRESH_INTERVAL;
            if (elapsedTime <= 0) {
                if (mTrainingModel.getExercises().size() > adapter.getMapPosition() + 1) {
                    adapter.setMapPosition(adapter.getMapPosition() + 1);
                    adapter.notifyDataSetChanged();
                    stopTimer();
                    sp.play(soundIdPoint, 1, 1, 0, 0, 1);
                    startMusicExercise(mTrainingModel.getExercises().get(adapter.getMapPosition()).getAudio());
                    //+1 sec
                    startTimer(mTrainingModel.getExercises().get(adapter.getMapPosition()).getTime() + 1000L);
                } else {
                    Toast.makeText(this, "Complete", Toast.LENGTH_SHORT).show();
                    stopTimer();
                }
            }
            tempTime[0] -= DEFAULT_REFRESH_INTERVAL;

            Log.e("startTimer: ", String.valueOf(elapsedTime));
            mBinding.contentTraining.timer.setText(FormatTime.formatTime(elapsedTime));
        }), DEFAULT_REFRESH_INTERVAL, DEFAULT_REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void setImageExercise(String image) {
        if (image != null) {
            Glide.with(getContext())
                    .load(Uri.parse(image))
                    .placeholder(R.mipmap.icon)
                    .fitCenter()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mBinding.contentTraining.imageTrainingExercise);
        }
    }

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
    public void close() {
        stopTimer();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
        stopTimer();
    }
}
