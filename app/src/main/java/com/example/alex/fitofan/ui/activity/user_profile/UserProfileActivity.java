package com.example.alex.fitofan.ui.activity.user_profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.ActivityProfileUserBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.ILoadingStatusUserPlans;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.interfaces.SaveStatus;
import com.example.alex.fitofan.interfaces.SubStatus;
import com.example.alex.fitofan.interfaces.UserStatus;
import com.example.alex.fitofan.models.GetPlansModel;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.User;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.settings.UserDataChangeActivity;
import com.example.alex.fitofan.ui.activity.sub.SubActivity;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.CountData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity implements ILoadingStatus<User>, ILoadingStatusUserPlans, LikeStatus, SubStatus, UserStatus {

    //TODO: It might be useful to use DataBinding
    //TODO: Будет хорошо, если вы будете использовать DataBinding
    private ActivityProfileUserBinding mBinding;
    private RecyclerAdapterUserProfile adapter;
    private User mUser;
    private boolean isSub;
    private Menu menu;
    private TextView countLike;
    private ImageView like;
    private int position;
    private GetPlansModel mPlans;
    private ImageView save;
    private ProgressDialog progressDialog;
    private TextView countSaved;
    private TextView countLikeAva;
    private ImageView likeAva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_user);
        setSupportActionBar(mBinding.toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        initListeners();
        initRecycler();
        initRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (getIntent().getStringExtra("uid").equals(
                new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
        )) {
            getMenuInflater().inflate(R.menu.user_profile_my, menu);
            return true;
        }
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_sub) {
            if (Connection.isNetworkAvailable(this)) {
                if (isSub) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                    map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                    map.put("user_id", mUser.getUid());
                    Request.getInstance().unSubscribeUser(map, this);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                    map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                    map.put("user_id", mUser.getUid());
                    Request.getInstance().subscribeUser(map, this);
                }
            }
        }

        if (id == R.id.action_like) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("avatar_id", mUser.getAvatarId());
            if (Connection.isNetworkAvailable(this)) {
                if (mUser.getAvatarLiked() == 1)
                    Request.getInstance().dislikeAvatar(map, this);
                if (mUser.getAvatarLiked() != 1)
                    Request.getInstance().likeAvatar(map, this);
            }
        }

        if (id == R.id.action_edit)

        {
            startActivity(new Intent(this, UserDataChangeActivity.class));
        }

        return super.

                onOptionsItemSelected(item);

    }

    protected void likePlan(String id, ImageView like, TextView countLike, boolean isButton, int position) {
        this.countLike = countLike;
        this.like = like;
        this.position = position;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        if (Connection.isNetworkAvailable(this)) {
            if (!isButton) {
                Request.getInstance().like(map, this);
            }
            if (isButton) {
                if (mPlans.getTrainings().get(position).getLiked() == 1)
                    Request.getInstance().dislikePlan(map, this);
                if (mPlans.getTrainings().get(position).getLiked() != 1)
                    Request.getInstance().like(map, this);
            }
        }
    }

    protected void likeAva(String id, ImageView likeAva, TextView countLikeAva, boolean isButton) {
        this.countLikeAva = countLikeAva;
        this.likeAva = likeAva;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("avatar_id", id);
        if (Connection.isNetworkAvailable(this)) {
            if (!isButton) {
                Request.getInstance().likeAvatar(map, this);
            }
            if (isButton) {
                if (mUser.getAvatarLiked() == 1)
                    Request.getInstance().dislikeAvatar(map, this);
                if (mUser.getAvatarLiked() != 1)
                    Request.getInstance().likeAvatar(map, this);
            }
        }
    }

    protected void savePlan(String id, ImageView save, TextView countSaved, int position) {
        this.countSaved = countSaved;
        this.save = save;
        this.position = position;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        if (Connection.isNetworkAvailable(this)) {
            if (mPlans.getTrainings().get(position).getIsSaved() == 1)
                Request.getInstance().unSavePlan(map, this);
            if (mPlans.getTrainings().get(position).getIsSaved() != 1)
                Request.getInstance().savePlan(map, this);
        }
    }

    private void initRequest() {
        if (Connection.isNetworkAvailable(this)) {
            mUser = new User();
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", getIntent().getStringExtra("uid"));
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            Request.getInstance().getUserData(map, this);
        }
    }

    private void initRecycler() {
        ArrayList<GetTrainingModel> models = new ArrayList<>();
        mBinding.content.rvUserProfile.setNestedScrollingEnabled(false);
        mBinding.content.rvUserProfile.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.content.rvUserProfile.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterUserProfile(this, mUser, models);
        mBinding.content.rvUserProfile.setAdapter(adapter);
    }

    private void initListeners() {
        mBinding.toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    protected void goSub() {
        Intent intent = new Intent(this, SubActivity.class);
        intent.putExtra("user_id", getIntent().getStringExtra("uid"));
        startActivity(intent);
    }

    @Override
    public void onSuccess(User info) {
        if (!new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid()
                .equals(getIntent().getStringExtra("uid"))
                ) {
            if (info.getSubscribed() == 1) {
                isSub = true;
                menu.getItem(0).setTitle(getResources().getString(R.string.unsubscribe));
            }
            if(info.getAvatarLiked() == 1){
                menu.getItem(0).setIcon(R.drawable.ic_favorite_full);
            }
        }
        mUser = info;
        adapter.setmUserModel(info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(info.getName() + " " + info.getSurname());

        if (Connection.isNetworkAvailable(this)) {
            HashMap<String, String> map2 = new HashMap<>();
            map2.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map2.put("user_id", getIntent().getStringExtra("uid"));
            map2.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            Request.getInstance().getUserPlans(map2, this);
        }
    }

    @Override
    public void onSuccess(GetPlansModel info) {
        mPlans = info;
        adapter.setmWallModels(info.getTrainings());
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    public void onSuccess(String info) {
        if (info.equals("true")) {
            menu.getItem(0).setTitle(getResources().getString(R.string.unsubscribe));
            isSub = true;
        }
        if (info.equals("false")) {
            menu.getItem(0).setTitle(getResources().getString(R.string.subscribe));
            isSub = false;
        }
        if (info.equals("like") || info.equals("dislike")) {
            if (info.equals("like")) {
                if (mPlans.getTrainings().get(position).getLiked() != 1) {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_full_red));
                    countLike.setText(CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) + 1))
                    );
                    countLike.setTextColor(Color.parseColor("#FFFFFF"));
                    adapter.getmWallModels().get(position).setLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) + 1));
                    mPlans.getTrainings().get(position).setLiked(1);
                }
            }
            if (info.equals("dislike")) {
                if (mPlans.getTrainings().get(position).getLiked() == 1) {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
                    countLike.setText(CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) - 1))
                    );
                    countLike.setTextColor(Color.parseColor("#000000"));
                    adapter.getmWallModels().get(position).setLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) - 1));
                    mPlans.getTrainings().get(position).setLiked(0);
                }
            }
            like.startAnimation(AnimationUtils.loadAnimation(this, R.animator.animation_scale_like));
        }

        if (info.equals("likeAvatar") || info.equals("dislikeAvatar")) {
            if (info.equals("likeAvatar")) {
                if (mUser.getAvatarLiked() != 1) {
                    menu.getItem(0).setIcon(R.drawable.ic_favorite_full);
//                    likeAva.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_full_red));
//                    countLikeAva.setText(CountData.mathLikes(String.valueOf(Integer.valueOf(mUser.getAvatarLikes()) + 1)));
//                    countLikeAva.setTextColor(Color.parseColor("#FFFFFF"));
                    mUser.setAvatarLikes(String.valueOf(Integer.valueOf(mUser.getAvatarLikes()) + 1));
                    mUser.setAvatarLiked(1);
                }
            }
            if (info.equals("dislikeAvatar")) {
                if (mUser.getAvatarLiked() == 1) {
                    menu.getItem(0).setIcon(R.drawable.ic_favorite);
//                    likeAva.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
//                    countLikeAva.setText(CountData.mathLikes(String.valueOf(Integer.valueOf(mUser.getAvatarLikes()) - 1)));
//                    countLikeAva.setTextColor(Color.parseColor("#000000"));
                    mUser.setAvatarLikes(String.valueOf(Integer.valueOf(mUser.getAvatarLikes()) - 1));
                    mUser.setAvatarLiked(0);
                }
            }
//            likeAva.startAnimation(AnimationUtils.loadAnimation(this, R.animator.animation_scale_like));
        }
        if (info.equals("save") || info.equals("unsave")) {
            if (info.equals("save")) {
                if (mPlans.getTrainings().get(position).getIsSaved() != 1) {
                    save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_full_black));
                    countSaved.setText(getResources().getString(R.string.saved) + ": " +
                            String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getSaved()) + 1)
                    );
                    adapter.getmWallModels().get(position).setSaved(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getSaved()) + 1));
                    mPlans.getTrainings().get(position).setIsSaved(1);
                }
            }
            if (info.equals("unsave")) {
                if (mPlans.getTrainings().get(position).getIsSaved() == 1) {
                    save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_black));
                    countSaved.setText(getResources().getString(R.string.saved) + ": " +
                            String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getSaved()) - 1)
                    );
                    adapter.getmWallModels().get(position).setSaved(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getSaved()) - 1));
                    mPlans.getTrainings().get(position).setIsSaved(0);
                }
            }
            save.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animation_scale_like));
        }

    }

    @Override
    public void onFailure(String message) {

    }
}