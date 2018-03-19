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
import android.text.method.ScrollingMovementMethod;
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
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_training);

        mPresenter = new TrainingPresenter(this);

        mBinding.content.descriptionExercise.setMovementMethod(new ScrollingMovementMethod());
        position = 0;
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

    protected class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_DISTANCE)
                return false;
            if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MIN_VELOCITY) {
                Toast.makeText(TrainingActivity.this, "-1", Toast.LENGTH_SHORT).show();
            } else if (e2.getY() + e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MIN_VELOCITY) {
                Toast.makeText(TrainingActivity.this, "+1", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

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
//    private void startMusicExercise(String audioUri) {
//        if (audioUri != null) {
//            Log.e("startMusicExercise: ", audioUri);
//            Uri bufUri = Uri.parse(audioUri);
//            try {
//                mMediaPlayer = new MediaPlayer();
//                mMediaPlayer.setDataSource(this, bufUri);
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.e("startMusicExercise: ", "beda");
//            Toast.makeText(getContext(), "No audio", Toast.LENGTH_SHORT).show();
//        }
//    }
//
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

    private void initListeners() {

        mSwipeDetector = new GestureDetectorCompat(this, new MyGestureListener());
        mBinding.content.swipeTraining.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mSwipeDetector.onTouchEvent(event);
            }
        });

        mBinding.content.btStart.setOnClickListener(v -> {
            if (!isRunning()) {
                Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
                startTimer(10000);
            }
        });

        mBinding.content.btReset.setOnClickListener(v -> {
            stopTimer();
            Toast.makeText(this, "Stoped", Toast.LENGTH_SHORT).show();
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
        mBinding.content.timer.setText(FormatTime.formatTime(tempTime[0]));
//        startMusicExercise(mTrainingModel.getExercises().get(adapter.getMapPosition()).getAudio());
        mTimerThread = Executors.newSingleThreadScheduledExecutor();
        mTimerThread.scheduleWithFixedDelay(() -> new Handler(Looper.getMainLooper()).post(() -> {
            long elapsedTime = tempTime[0] - DEFAULT_REFRESH_INTERVAL;
            if (elapsedTime <= 0) {
                stopTimer();
            }
            tempTime[0] -= DEFAULT_REFRESH_INTERVAL;

            Log.e("startTimer: ", String.valueOf(elapsedTime));
            mBinding.content.timer.setText(FormatTime.formatTime(elapsedTime));
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
                    .into(mBinding.content.imageExercise);
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
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
        stopTimer();
    }
}
