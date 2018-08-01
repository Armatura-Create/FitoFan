package com.example.alex.fitofan.ui.activity.preview_plan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityPlanPreviewBinding;
import com.example.alex.fitofan.interfaces.DelStatus;
import com.example.alex.fitofan.interfaces.ILoadingEdit;
import com.example.alex.fitofan.interfaces.ILoadingPlan;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.interfaces.UserStatus;
import com.example.alex.fitofan.models.GetExerciseModel;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.PhotoModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.ui.activity.training.TrainingActivity;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.CustomDialog.CustomDialog;
import com.example.alex.fitofan.utils.ItemClickSupport;
import com.example.alex.fitofan.utils.StaticValues;
import com.example.alex.fitofan.utils.UnpackingTraining;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class PreviewPlanActivity extends AppCompatActivity implements ILoadingPlan, LikeStatus, DelStatus, UserStatus, ILoadingEdit {

    private ActivityPlanPreviewBinding mBinding;
    private RecyclerAdapterPreviewPlan adapter;
    private GetPlanModel mPlanModel;
    private GetPlanModel mNotEditPlan;

    private Menu menu;
    private int positionLike = 1;
    private int positionSave = 0;
    private int tempPosition;
    private ArrayList<PhotoModel> imageTraining = new ArrayList<>();

    private static final int FILE_SELECT_CODE = 400;
    private LinearLayout borderLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_preview);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_plan_preview);
        setSupportActionBar(mBinding.toolbar);
        ArrayList<GetExerciseModel> models = new ArrayList<>();
        mPlanModel = new GetPlanModel();
        mPlanModel.setExercises(models);
        initRecyclerView();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initRequest() {
        if (Connection.isNetworkAvailable(getContext())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("plan_id", getIntent().getStringExtra("planId"));
            Request.getInstance().getPlan(map, this);
        } else {
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIntent().getStringExtra("userId").equals(
                new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
        )) {
            getMenuInflater().inflate(R.menu.preview_plan_wall_with_del, menu);
            initRequest();
            return true;
        }
        getMenuInflater().inflate(R.menu.preview_plan_wall, menu);
        initRequest();
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
            map.put("plan_id", String.valueOf(mPlanModel.getTraining().getId()));
            if (Connection.isNetworkAvailable(this)) {
                if (mPlanModel.getTraining().getIsSaved() == 0)
                    Request.getInstance().savePlan(map, this);
                if (mPlanModel.getTraining().getIsSaved() != 0)
                    Request.getInstance().unSavePlan(map, this);
            }
            return true;
        }
        if (id == R.id.action_edit_server) {
            goEdit();
        }

        if (id == R.id.action_remove_all) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("plan_id", String.valueOf(mPlanModel.getTraining().getId()));
            Request.getInstance().delPlan(map, this);
        }

        if (id == R.id.action_remove_wall) {
            if (mPlanModel.getStatus() == 1)
                unpublicPlan(String.valueOf(mPlanModel.getTraining().getId()));
        }

        if (id == R.id.action_music_server) {
            if (mPlanModel.getTraining().getParentId().equals("0")) {
                if (mPlanModel.getTraining().getIsSaved() == 1) {
                    copyPlan();
                } else {
                    Toast.makeText(this, "save first plan", Toast.LENGTH_SHORT).show();
                }
            }

            if (mPlanModel.getTraining().getIsSaved() == 1 || !mPlanModel.getTraining().getParentId().equals("0")) {
                if (mPlanModel.getExercises().size() > 0) {
                    int[] pos = {0};
                    Dialog dialog = CustomDialog.cardMusic(this,
                            mPlanModel.getExercises().get(pos[0]).getName(),
                            mPlanModel.getExercises().get(pos[0]).getImage(),
                            mPlanModel.getExercises().get(pos[0]).getMusicUrls() != null);
                    dialog.setCancelable(true);
                    TextView posExercise = dialog.findViewById(R.id.tv_exercise_now);
                    TextView allCountExercise = dialog.findViewById(R.id.tv_count_exercises);
                    posExercise.setText(pos[0] + 1 + "");
                    allCountExercise.setText(mPlanModel.getExercises().size() + "");
                    dialog.findViewById(R.id.bt_add_audio_exercise).setOnClickListener(view ->
                            chooseAudioExercise(pos[0], dialog.findViewById(R.id.background_border_audio)));

                    if (mPlanModel.getExercises().size() - 1 == pos[0])
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.VISIBLE);
                    if (pos[0] == 0)
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.VISIBLE);
                    dialog.findViewById(R.id.next_exercise).setOnClickListener(view -> {
                        if (mPlanModel.getExercises().size() - 1 > pos[0]) {
                            pos[0]++;
                            if (mPlanModel.getExercises().size() > pos[0]) {
                                CustomDialog.cardSetMusic(dialog, mPlanModel.getExercises().get(pos[0]).getName(),
                                        mPlanModel.getExercises().get(pos[0]).getImage(),
                                        (mPlanModel.getExercises().get(pos[0]).getMusicUrls() != null &&
                                                !mPlanModel.getExercises().get(pos[0]).getMusicUrls().equals("")));
                                posExercise.setText(pos[0] + 1 + "");
                            }
                        }
                        if (mPlanModel.getExercises().size() - 1 == pos[0])
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
                                CustomDialog.cardSetMusic(dialog, mPlanModel.getExercises().get(pos[0]).getName(),
                                        mPlanModel.getExercises().get(pos[0]).getImage(),
                                        (mPlanModel.getExercises().get(pos[0]).getMusicUrls() != null &&
                                                mPlanModel.getExercises().get(pos[0]).getMusicUrls() != null));
                                posExercise.setText(pos[0] + 1 + "");
                            }
                        }
                        if (mPlanModel.getExercises().size() - 1 == pos[0])
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void goEdit() {
        Intent intent = new Intent(getContext(), CreatePlanActivity.class);
        intent.putExtra("edit", true);
        intent.putExtra("planId", mPlanModel.getTraining().getId());
        startActivity(intent);
    }

    private void copyPlan() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", String.valueOf(mPlanModel.getTraining().getId()));
        Request.getInstance().copyPlan(map, this);
    }

    void chooseAudioExercise(int position, LinearLayout borderLinear) {
        tempPosition = position;
        this.borderLinear = borderLinear;
        Intent audioPickerIntent;
        audioPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        audioPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        audioPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
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
                            mPlanModel.getExercises().get(tempPosition).setMusicUrls(String.valueOf(uriExercise));
                            try {
                                editMusicExercise(mPlanModel.getExercises().get(tempPosition).getId());
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Log.e("startMusicExercise", String.valueOf(uriExercise));
                            borderLinear.setVisibility(View.VISIBLE);
                        }
                }
                break;
        }
    }

    private void editMusicExercise(String id) throws UnsupportedEncodingException {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("music_urls", URLEncoder.encode(mPlanModel.getExercises().get(tempPosition).getMusicUrls(), "UTF-8"));
        map.put("id", mPlanModel.getExercises().get(tempPosition).getId());
        map.put("name", mPlanModel.getExercises().get(tempPosition).getName());
        map.put("description", mPlanModel.getExercises().get(tempPosition).getDescription());
        map.put("plan_id", mPlanModel.getTraining().getId());
        map.put("recovery_time", String.valueOf(mPlanModel.getExercises().get(tempPosition).getRecoveryTime()));
        map.put("count_repetitions", String.valueOf(mPlanModel.getExercises().get(tempPosition).getCountRepetitions()));
        map.put("exercise_time", String.valueOf(mPlanModel.getExercises().get(tempPosition).getTime()));
        map.put("time_between", String.valueOf(mPlanModel.getExercises().get(tempPosition).getTimeBetween()));
        map.put("image", "");
        Request.getInstance().editExercises(map, this);
    }

    private void like() {
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", String.valueOf(mPlanModel.getTraining().getId()));
        if (Connection.isNetworkAvailable(this)) {
            if (mPlanModel.getTraining().getLiked() == 0)
                Request.getInstance().like(map, this);
            if (mPlanModel.getTraining().getLiked() != 0)
                Request.getInstance().dislikePlan(map, this);
        }
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mBinding.goToTraining.setOnClickListener(v ->
                goTraining());

        ItemClickSupport.addTo(mBinding.rv).setOnItemClickListener((recyclerView, position, v) -> {
            if (position == 0) {
                Dialog dialog = CustomDialog.card(this, this.getWindow(),
                        mPlanModel.getTraining().getName(),
                        mPlanModel.getTraining().getDescription(),
                        imageTraining, null);
                dialog.setCancelable(true);
                dialog.findViewById(R.id.liner_bottom).setVisibility(View.GONE);
            }
            if (position != 0) {

                int[] pos = {adapter.getModel().get(position - 1).getPosition()};
                if (!adapter.getModel().get(position - 1).isRest()) {
                    Dialog dialog = CustomDialog.card(this, this.getWindow(),
                            mPlanModel.getExercises().get(pos[0]).getName(),
                            mPlanModel.getExercises().get(pos[0]).getDescription(),
                            mPlanModel.getExercises().get(pos[0]).getPhotos(),
                            mPlanModel.getExercises().get(pos[0]).getVideoUrl());
                    dialog.setCancelable(true);
                    TextView posExercise = dialog.findViewById(R.id.tv_exercise_now);
                    TextView allCountExercise = dialog.findViewById(R.id.tv_count_exercises);
                    posExercise.setText(pos[0] + 1 + "");
                    allCountExercise.setText(mPlanModel.getExercises().size() + "");
                    if (mPlanModel.getExercises().size() - 1 == pos[0])
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.next_exercise).setVisibility(View.VISIBLE);
                    if (pos[0] == 0)
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.INVISIBLE);
                    else
                        dialog.findViewById(R.id.back_exercise).setVisibility(View.VISIBLE);
                    dialog.findViewById(R.id.next_exercise).setOnClickListener(view -> {
                        if (mPlanModel.getExercises().size() - 1 > pos[0]) {
                            pos[0]++;
                            if (mPlanModel.getExercises().size() > pos[0]) {
                                CustomDialog.cardSet(dialog, mPlanModel.getExercises().get(pos[0]).getName(),
                                        mPlanModel.getExercises().get(pos[0]).getDescription(),
                                        mPlanModel.getExercises().get(pos[0]).getPhotos(),
                                        mPlanModel.getExercises().get(pos[0]).getVideoUrl());
                                posExercise.setText(pos[0] + 1 + "");
                            }
                        }
                        if (mPlanModel.getExercises().size() - 1 == pos[0])
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
                                CustomDialog.cardSet(dialog, mPlanModel.getExercises().get(pos[0]).getName(),
                                        mPlanModel.getExercises().get(pos[0]).getDescription(),
                                        mPlanModel.getExercises().get(pos[0]).getPhotos(),
                                        mPlanModel.getExercises().get(pos[0]).getVideoUrl());
                                posExercise.setText(pos[0] + 1 + "");
                            }
                        }
                        if (mPlanModel.getExercises().size() - 1 == pos[0])
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
        adapter = new RecyclerAdapterPreviewPlan(this, UnpackingTraining.buildExercises(mPlanModel));
        mBinding.rv.setAdapter(adapter);

    }

    void goTraining() {
        if (getIntent().getBooleanExtra("isWall", false)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("planModel", mNotEditPlan);
            Intent intent = new Intent(getContext(), TrainingActivity.class);
            intent.putExtra("planModel", bundle);
            startActivity(intent);
        }
    }

    public void unpublicPlan(String id) {
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
        mBinding.collapsing.setTitle(mPlanModel.getTraining().getName());

        Glide.with(getContext().getApplicationContext())
                .load(Uri.parse(mPlanModel.getTraining().getImage()))
                .apply(diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .transition(withCrossFade())
                .into(mBinding.traningImage);

    }

    private void imagesSet() {
        PhotoModel temp = new PhotoModel();
        temp.setImagePath(mPlanModel.getTraining().getImage());
        imageTraining.clear();
        imageTraining.add(temp);
        Log.e("imagesSet: ", new Gson().toJson(imageTraining));

        for (int i = 0; i < mPlanModel.getExercises().size(); i++) {
            ArrayList<PhotoModel> temps = new ArrayList<>();
            PhotoModel temp2 = new PhotoModel();
            if (mPlanModel.getExercises().get(i).getImage() != null && !mPlanModel.getExercises().get(i).getImage().equals("")) {
                temp2.setImagePath(mPlanModel.getExercises().get(i).getImage());
                temps.add(temp2);
                temps.addAll(mPlanModel.getExercises().get(i).getPhotos());
                mPlanModel.getExercises().get(i).setPhotos(temps);
            } else {
                mPlanModel.getExercises().get(i).setPhotos(null);
            }
        }
    }

    public Context getContext() {
        return this;
    }

    @Override
    public void onSuccess(GetPlanModel info, String request) {
        if (request.equals("getPlan") && info != null) {
            mPlanModel = info;
            if (info.getTraining() != null) {
                imagesSet();
                setData();
                mNotEditPlan = info;
                adapter.setModel(UnpackingTraining.buildExercisesPreview(mPlanModel));
                adapter.setTrainingModel(info);
                adapter.notifyDataSetChanged();
                if (menu != null) {
                    if (info.getTraining().getIsSaved() == 1) {
                        menu.getItem(positionSave).setIcon(getResources().getDrawable(R.drawable.ic_save_full));
                    }

                    if (info.getTraining().getLiked() == 1) {
                        menu.findItem(R.id.action_like).setIcon(getResources().getDrawable(R.drawable.ic_favorite_full));
                    }
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

        if (request.equals("copyPlan")) {
            mPlanModel = info;
        }
        mBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String info, String request) {
        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String info) {
        if (info.equals("like") || info.equals("dislike")) {
            if (info.equals("like")) {
                menu.getItem(positionLike).setIcon(getResources().getDrawable(R.drawable.ic_favorite_full));
                mPlanModel.getTraining().setLiked(1);
                return;
            }
            menu.getItem(positionLike).setIcon(getResources().getDrawable(R.drawable.ic_favorite));
            mPlanModel.getTraining().setLiked(0);
            return;
        }

        if (info.equals("save") || info.equals("unsave")) {
            if (info.equals("save")) {
                menu.getItem(positionSave).setIcon(getResources().getDrawable(R.drawable.ic_save_full));
                mPlanModel.getTraining().setIsSaved(1);
                return;
            }

            menu.getItem(positionSave).setIcon(getResources().getDrawable(R.drawable.ic_save));
            mPlanModel.getTraining().setIsSaved(0);
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
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, StaticValues.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
    }
}