package com.example.alex.fitofan.ui.activity.training;

import android.app.Dialog;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.ActivityTrainingBinding;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.CustomDialog;
import com.example.alex.fitofan.utils.CustomFontsLoader;
import com.example.alex.fitofan.utils.FormatTime;
import com.example.alex.fitofan.utils.ItemClickSupport;
import com.example.alex.fitofan.utils.StaticValues;
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
    private RecyclerAdapter adapter;
    private int mPosition = 0;
    private boolean isStop = true;
    private int positionMusic = -1;
    private float volume = (float) 0.6;

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

    private void initSoundPoint() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);

        soundIdPoint = sp.load(this, R.raw.point, 1);

    }

    private void startMusicExercise(String audioUri) {
        if (audioUri != null) {
            Uri bufUri = Uri.parse(audioUri);
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(this, bufUri);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(volume, volume);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed, please re-select music", Toast.LENGTH_SHORT).show();
            }
        } else {
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
        setData();
    }

    private void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(this, UnpackingTraining.buildExercises(mTrainingModel));
        mBinding.content.rv.setAdapter(adapter);
    }

    private void initListeners() {
        try {
            mBinding.content.timer.setTypeface(CustomFontsLoader.getTypeface(this, 0));
        } catch (Exception e) {
            Log.e("initListeners: ", String.valueOf(e));
        }


        mBinding.content.btStart.setOnClickListener(v -> {
            if (!isRunning() && isStop) {
                mBinding.content.btStart.setText("DONE");
                mBinding.content.btReset.setText("STOP");
                preparationTimer();
            } else if (!isStop && !isRunning()) {
                if (mPosition < adapter.getItemCount() - 1) {
                    mPosition++;
                    ifMethod();
                    sp.play(soundIdPoint, 1, 1, 0, 0, 1);
                } else {
                    isStop = true;
                    releaseMP();
                    Toast.makeText(getContext(), "All done", Toast.LENGTH_SHORT).show();
                    mBinding.content.btStart.setText("START");
                    mBinding.content.btReset.setText("RESET");
                    positionMusic = -1;
                }
            }
        });

        mBinding.content.btReset.setOnClickListener(v -> {
            if (!isRunning() && isStop) {

                Dialog dialog = CustomDialog.dialogSimple(getContext(),
                        null,
                        "Reset?",
                        "Yes",
                        "No");
                dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                    mPosition = 0;
                    setData();
                    positionMusic = -1;
                    dialog.dismiss();
                });
            } else {
                Dialog dialog = CustomDialog.dialogSimple(getContext(),
                        null,
                        "Stop?",
                        "Yes",
                        "No");
                dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                    isStop = true;
                    mBinding.content.btStart.setText("START");
                    mBinding.content.btReset.setText("RESET");
                    stopTimer();
                    releaseMP();
                    positionMusic = -1;
                    dialog.dismiss();
                });
            }
        });

        ItemClickSupport.addTo(mBinding.content.rv).setOnItemClickListener((recyclerView, position, v) -> {
            if (!isRunning() && isStop) {
                mPosition = position;
                setData();
            } else {
                Toast.makeText(this, "First stop the current exercise", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ifMethod() {
        setData();
        if (mPosition < adapter.getItemCount()) {

            if (!adapter.getModel().get(mPosition).isRest() && positionMusic != adapter.getModel().get(mPosition).getPosition()) {
                releaseMP();
                positionMusic = adapter.getModel().get(mPosition).getPosition();
                startMusicExercise(mTrainingModel.getExercises().get(positionMusic).getAudio());
            } else if (!adapter.getModel().get(mPosition).isRest()) {
                playMP();
            }

            if (adapter.getModel().get(mPosition).getTime() % 10 == 0) {
                startTimer(!adapter.getModel().get(mPosition).isRest() ?
                        adapter.getModel().get(mPosition).getTime() / 10 :
                        adapter.getModel().get(mPosition).getTime());
            } else {
                isStop = false;
            }
        }

    }

    private void preparationTimer() {
        final long[] preparationTime = {5000L};
        mBinding.content.tvNameExercise.setText(getResources().getString(R.string.prepare_time));
        mBinding.content.type.setText("Time");
        mBinding.content.descriptionExercise.setText("");
        mBinding.content.timer.setText(FormatTime.formatTime(preparationTime[0]));
        mTimerThread = Executors.newSingleThreadScheduledExecutor();
        mTimerThread.scheduleWithFixedDelay(() -> new Handler(Looper.getMainLooper()).post(() -> {
            long elapsedTime = preparationTime[0] - DEFAULT_REFRESH_INTERVAL;
            if (elapsedTime <= 0) {
                stopTimer();
                sp.play(soundIdPoint, 1, 1, 0, 0, 1);
                ifMethod();
            } else {
                preparationTime[0] -= DEFAULT_REFRESH_INTERVAL;
                mBinding.content.timer.setText(FormatTime.formatTime(elapsedTime));
            }
        }), DEFAULT_REFRESH_INTERVAL, DEFAULT_REFRESH_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void startTimer(long timerStart) {

        final long[] tempTime = {timerStart};
        mTimerThread = Executors.newSingleThreadScheduledExecutor();
        mTimerThread.scheduleWithFixedDelay(() -> new Handler(Looper.getMainLooper()).post(() -> {
            long elapsedTime = tempTime[0] - DEFAULT_REFRESH_INTERVAL;
            if (elapsedTime <= 0) {
                mPosition++;
                stopTimer();
                if (mPosition < adapter.getItemCount()) {
                    ifMethod();
                } else {
                    stopTimer();
                    releaseMP();
                    Toast.makeText(getContext(), "All done", Toast.LENGTH_SHORT).show();
                    mBinding.content.btStart.setText("START");
                    mBinding.content.btReset.setText("RESET");
                    isStop = true;
                }
                sp.play(soundIdPoint, 1, 1, 0, 0, 1);
            } else {
                tempTime[0] -= DEFAULT_REFRESH_INTERVAL;
                mBinding.content.timer.setText(FormatTime.formatTime(elapsedTime));
            }
        }), DEFAULT_REFRESH_INTERVAL, DEFAULT_REFRESH_INTERVAL, TimeUnit.MILLISECONDS);

    }

    private void setData() {

        mBinding.content.rv.smoothScrollToPosition(mPosition);

        if (!adapter.getModel().get(mPosition).isRest() &&
                adapter.getModel().get(mPosition).getImage() != null) {
            mBinding.content.imageExercise.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getContext())
                    .load(Uri.parse(adapter.getModel().get(mPosition).getImage()))
                    .into(mBinding.content.imageExercise);
        } else if (adapter.getModel().get(mPosition).isRest()) {
            mBinding.content.imageExercise.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(getContext())
                    .load(R.drawable.icon_fitofan)
                    .into(mBinding.content.imageExercise);
        } else {
            mBinding.content.imageExercise.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(getContext())
                    .load(R.drawable.logo_fitofan)
                    .into(mBinding.content.imageExercise);
        }

        mBinding.content.tvNameExercise.setText(adapter.getModel().get(mPosition).getName());

        if (!adapter.getModel().get(mPosition).isRest())
            mBinding.content.descriptionExercise.setText(adapter.getModel().get(mPosition).getDescription());
        else
            mBinding.content.descriptionExercise.setText(getResources().getString(R.string.rest));

        String temp = null;
        switch ((int) (adapter.getModel().get(mPosition).getTime() % 10)) {
            case StaticValues.TIME:
                if (!adapter.getModel().get(mPosition).isRest())
                    mBinding.content.timer.setText(FormatTime.formatCount(adapter.getModel().get(mPosition).getTime()));
                else
                    mBinding.content.timer.setText(FormatTime.formatTime(adapter.getModel().get(mPosition).getTime()));

                temp = "Time";
                break;
            case StaticValues.DISTANCE:
                mBinding.content.timer.setText(FormatTime.formatCount(adapter.getModel().get(mPosition).getTime()));
                temp = "Distance" + " (" + FormatTime.formatType(adapter.getModel().get(mPosition).getTime()) + ")";
                break;
            case StaticValues.WEIGHT:
                mBinding.content.timer.setText(FormatTime.formatCount(adapter.getModel().get(mPosition).getTime()));
                temp = "Weight" + " (" + FormatTime.formatType(adapter.getModel().get(mPosition).getTime()) + ")";
                break;
            case StaticValues.COUNT:
                mBinding.content.timer.setText(FormatTime.formatCount(adapter.getModel().get(mPosition).getTime()));
                temp = "Count" + " (" + FormatTime.formatType(adapter.getModel().get(mPosition).getTime()) + ")";
                break;
        }
        mBinding.content.type.setText(temp);
        adapter.notifyDataSetChanged();
    }

    private void pauseMP() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.pause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void playMP() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        releaseMP();
        stopTimer();
        super.onDestroy();
    }

    public int getPosition() {
        return mPosition;
    }
}
