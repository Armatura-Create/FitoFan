package com.example.alex.fitofan.ui.activity.preview_plan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityPlanPreviewBinding;
import com.example.alex.fitofan.interfaces.DelStatus;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.interfaces.UserStatus;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.training.TrainingActivity;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.CustomDialog.CustomDialog;
import com.example.alex.fitofan.utils.ItemClickSupport;
import com.example.alex.fitofan.utils.StaticValues;
import com.example.alex.fitofan.utils.UnpackingTraining;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class PreviewPlanActivity extends AppCompatActivity implements ILoadingStatus<GetPlanModel>, LikeStatus, DelStatus, UserStatus {

    private ActivityPlanPreviewBinding mBinding;
    private RecyclerAdapterPreviewPlan adapter;
    private TrainingModel mModel;
    private GetPlanModel mModelfromServer;

    private Menu menu;
    private ProgressDialog progressDialog;
    private boolean like;
    private boolean isSave;
    private int positionLike = 1;
    private int positionSave = 0;
    private int tempPosition;

    private static final int FILE_SELECT_CODE = 400;
    private LinearLayout borderLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_preview);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_plan_preview);
        setSupportActionBar(mBinding.toolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        ArrayList<ExerciseModel> models = new ArrayList<>();
        mModel = new TrainingModel();
        mModel.setExercises(models);
        initRecyclerView();
        if (Connection.isNetworkAvailable(getContext())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("plan_id", getIntent().getStringExtra("planId"));
            Request.getInstance().getPlan(map, this);
        } else {
            progressDialog.cancel();
        }
        initListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIntent().getStringExtra("userId").equals(
                new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
        )) {
            getMenuInflater().inflate(R.menu.preview_plan_wall_with_del, menu);
            return true;
        }
        getMenuInflater().inflate(R.menu.preview_plan_wall, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_like) {
            like();
            return true;
        }
        if (id == R.id.action_save) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("plan_id", String.valueOf(mModel.getId()));
            if (Connection.isNetworkAvailable(this)) {
                if (!isSave)
                    Request.getInstance().savePlan(map, this);
                if (isSave)
                    Request.getInstance().unSavePlan(map, this);
            }
            return true;
        }
        if (id == R.id.action_edit_server) {

        }

        if (id == R.id.action_remove_all) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("plan_id", String.valueOf(mModel.getId()));
            Request.getInstance().delPlan(map, this);
        }

        if (id == R.id.action_remove_wall) {
            if (mModelfromServer.getStatus() == 1)
                unpublicationPlan(String.valueOf(mModel.getId()));
        }

        if (id == R.id.action_music_server) {
            if (mModelfromServer.getExercises().size() > 0) {
                int[] pos = {0};
                Dialog dialog = CustomDialog.cardMusic(this,
                        mModelfromServer.getExercises().get(pos[0]).getName(),
                        mModelfromServer.getExercises().get(pos[0]).getImage());
                dialog.setCancelable(true);
                dialog.findViewById(R.id.bt_add_audio_exercise).setOnClickListener(view -> {
                    chooseAudioExercise(pos[0], dialog.findViewById(R.id.background_border_audio));
                });

                if (mModelfromServer.getExercises().size() - 1 == pos[0])
                    dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                else
                    dialog.findViewById(R.id.next_exercise).setVisibility(View.VISIBLE);
                if (pos[0] == 0)
                    dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
                else
                    dialog.findViewById(R.id.back_exercise).setVisibility(View.VISIBLE);
                dialog.findViewById(R.id.next_exercise).setOnClickListener(view -> {
                    if (mModelfromServer.getExercises().size() - 1 > pos[0]) {
                        pos[0]++;
                        if (mModelfromServer.getExercises().size() > pos[0]) {
                            CustomDialog.cardSetMusic(dialog, mModel.getExercises().get(pos[0]).getName(),
                                    mModelfromServer.getExercises().get(pos[0]).getImage(),
                                    mModel.getExercises().get(pos[0]).getAudio()!= null);
                        }
                    }
                    if (mModel.getExercises().size() - 1 == pos[0])
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.VISIBLE);
                    if (pos[0] == 0)
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.VISIBLE);
                });

                dialog.findViewById(R.id.back_exercise).setOnClickListener(view -> {
                    if (pos[0] > 0) {
                        pos[0]--;
                        if (pos[0] + 1 > 0) {
                            CustomDialog.cardSetMusic(dialog, mModel.getExercises().get(pos[0]).getName(),
                                    mModel.getExercises().get(pos[0]).getImage(),
                                    mModel.getExercises().get(pos[0]).getAudio()!= null);
                        }
                    }
                    if (mModel.getExercises().size() - 1 == pos[0])
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.VISIBLE);
                    if (pos[0] == 0)
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.VISIBLE);
                });
            }
        }

        return super.onOptionsItemSelected(item);

    }

    void chooseAudioExercise(int position, LinearLayout borderLinear) {
        tempPosition = position;
        this.borderLinear = borderLinear;
        Intent audioPickerIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            audioPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            audioPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            audioPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            audioPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        audioPickerIntent.setType("audio/*");
        audioPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(audioPickerIntent, "Select a File to Upload"), FILE_SELECT_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null)
                        if (data.getData() != null) {
                            Uri uriExercise = data.getData();
                            mModel.getExercises().get(tempPosition).setAudio(String.valueOf(uriExercise));
                            borderLinear.setVisibility(View.VISIBLE);
                        }
                }
                break;
        }
    }

    private void like() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", String.valueOf(mModel.getId()));
        if (Connection.isNetworkAvailable(this)) {
            if (!like)
                Request.getInstance().like(map, this);
            if (like)
                Request.getInstance().dislikePlan(map, this);
        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mBinding.goToTraining.setOnClickListener(v -> {
            goTraining();
        });

        ItemClickSupport.addTo(mBinding.rv).setOnItemClickListener((recyclerView, position, v) -> {
            if (position == 0) {
                Dialog dialog = CustomDialog.card(this, this.getWindow(),
                        mModel.getName(),
                        mModel.getDescription(),
                        mModel.getImage());
                dialog.setCancelable(true);
                dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
            }
            if (position != 0) {

                int[] pos = {adapter.getModel().get(position - 1).getPosition()};
                if (!adapter.getModel().get(position - 1).isRest()) {
                    Dialog dialog = CustomDialog.card(this, this.getWindow(),
                            mModel.getExercises().get(pos[0]).getName(),
                            mModel.getExercises().get(pos[0]).getDescription(),
                            mModel.getExercises().get(pos[0]).getImage());
                    dialog.setCancelable(true);
                    if (mModel.getExercises().size() - 1 == pos[0])
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.VISIBLE);
                    if (pos[0] == 0)
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.VISIBLE);
                    dialog.findViewById(R.id.next_exercise).setOnClickListener(view -> {
                        if (mModel.getExercises().size() - 1 > pos[0]) {
                            pos[0]++;
                            if (mModel.getExercises().size() > pos[0]) {
                                CustomDialog.cardSet(dialog, mModel.getExercises().get(pos[0]).getName(),
                                        mModel.getExercises().get(pos[0]).getDescription(),
                                        mModel.getExercises().get(pos[0]).getImage());

                            }
                        }
                        if (mModel.getExercises().size() - 1 == pos[0])
                            dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                        else
                            dialog.findViewById(R.id.next_exercise).setVisibility(View.VISIBLE);
                        if (pos[0] == 0)
                            dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
                        else
                            dialog.findViewById(R.id.back_exercise).setVisibility(View.VISIBLE);
                    });

                    dialog.findViewById(R.id.back_exercise).setOnClickListener(view -> {
                        if (pos[0] > 0) {
                            pos[0]--;
                            if (pos[0] + 1 > 0) {
                                CustomDialog.cardSet(dialog, mModel.getExercises().get(pos[0]).getName(),
                                        mModel.getExercises().get(pos[0]).getDescription(),
                                        mModel.getExercises().get(pos[0]).getImage());
                            }
                        }
                        if (mModel.getExercises().size() - 1 == pos[0])
                            dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                        else
                            dialog.findViewById(R.id.next_exercise).setVisibility(View.VISIBLE);
                        if (pos[0] == 0)
                            dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
                        else
                            dialog.findViewById(R.id.back_exercise).setVisibility(View.VISIBLE);
                    });
                }
            }
        });

    }

    private void initRecyclerView() {

        mBinding.rv.setHasFixedSize(false);
        mBinding.rv.setNestedScrollingEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterPreviewPlan(this, UnpackingTraining.buildExercises(mModel));
        mBinding.rv.setAdapter(adapter);

    }

    void goTraining() {
        if (getIntent().getBooleanExtra("isWall", false)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("traningModel", mModel);
            Intent intent = new Intent(getContext(), TrainingActivity.class);
            intent.putExtra("traningModel", bundle);
            startActivity(intent);
        }
    }

    public void unpublicationPlan(String id) {
        HashMap<String, String> map = new HashMap<>();
        if (Connection.isNetworkAvailable(getContext())) {
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("plan_id", id);
            map.put("status", "0");
            Request.getInstance().changePlanStatus(map, this);
        }
    }

    private void setData() {
        mBinding.collapsing.setTitle(mModelfromServer.getTraining().getName());

        Glide.with(getContext().getApplicationContext())
                .load(Uri.parse(mModelfromServer.getTraining().getImage()))
                .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .transition(withCrossFade())
                .into(mBinding.traningImage);

    }

    public Context getContext() {
        return this;
    }

    @Override
    public void onSuccess(GetPlanModel info) {
        if (info.getTraining() != null) {
            adapter.setModel(UnpackingTraining.buildExercises(setTraining(info)));
            adapter.setTrainingModel(setTraining(info));
            adapter.notifyDataSetChanged();
            if (info.getTraining().getIsSaved() == 1) {
                menu.getItem(positionSave).setIcon(getResources().getDrawable(R.drawable.ic_save_full));
                isSave = true;
            }

            if (info.getTraining().getLiked() == 1) {
                menu.getItem(positionLike).setIcon(getResources().getDrawable(R.drawable.ic_favorite_full));
                like = true;
            }

            if (info.getTraining().getUserId() != null) {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", info.getTraining().getUserId());
                map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                Request.getInstance().getUserData(map, this);
            }
        }
    }

    private TrainingModel setTraining(GetPlanModel training) {
        mModelfromServer = training;
        ArrayList<ExerciseModel> exerciseModels = new ArrayList<>();
        mModel = new TrainingModel();
        for (int i = 0; i < training.getExercises().size(); i++) {
            ExerciseModel exerciseModel = new ExerciseModel();
            exerciseModel.setCountRepetitions(Integer.valueOf(training.getExercises().get(i).getCountRepetitions()));
            exerciseModel.setImage(training.getExercises().get(i).getImage());
            exerciseModel.setDescription(training.getExercises().get(i).getDescription());
            exerciseModel.setName(training.getExercises().get(i).getName());
            exerciseModel.setRecoveryTime(Long.valueOf(training.getExercises().get(i).getRecoveryTime()));
            exerciseModel.setTime(Long.valueOf(training.getExercises().get(i).getTime()));
            exerciseModel.setTimeBetween(Long.valueOf(training.getExercises().get(i).getTimeBetween()));
            exerciseModels.add(exerciseModel);
        }
        mModel.setExercises(exerciseModels);
        mModel.setTime(Long.valueOf(training.getTraining().getPlan_time()));
        mModel.setName(training.getTraining().getName());
        mModel.setDescription(training.getTraining().getDescription());
        mModel.setImage(training.getTraining().getImage());
        mModel.setId(Integer.valueOf(training.getTraining().getId()));
        mModel.setCreatedTime(training.getTraining().getCreationDate());
        setData();
        return mModel;
    }

    @Override
    public void onSuccess(String info) {
        if (info.equals("like") || info.equals("dislike")) {
            if (info.equals("like")) {
                menu.getItem(positionLike).setIcon(getResources().getDrawable(R.drawable.ic_favorite_full));
                like = true;
                return;
            }
            menu.getItem(positionLike).setIcon(getResources().getDrawable(R.drawable.ic_favorite));
            like = false;
            return;
        }

        if (info.equals("save") || info.equals("unsave")) {
            if (info.equals("save")) {
                menu.getItem(positionSave).setIcon(getResources().getDrawable(R.drawable.ic_save_full));
                isSave = true;
                return;
            }

            menu.getItem(positionSave).setIcon(getResources().getDrawable(R.drawable.ic_save));
            isSave = false;
            return;
        }

        if (info.equals("deleted")) {
            onBackPressed();
        }

        if (info.equals("statusChange")) {
            Toast.makeText(this, getResources().getString(R.string.removed_from_wall), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSuccess(User info) {
        if (info != null) {
            adapter.setUserModel(info);
            adapter.notifyItemChanged(0);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, StaticValues.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }
}