package com.example.alex.fitofan.ui.fragments.my_plans;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.FragmentMyPlansBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatusMyPlans;
import com.example.alex.fitofan.interfaces.ILoadingStatusUserPlans;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.models.GetPlansModel;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.CountData;
import com.example.alex.fitofan.utils.PreCachingLayoutManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPlansFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ILoadingStatusUserPlans, ILoadingStatusMyPlans, LikeStatus {

    private FragmentMyPlansBinding mBinding;
    private View view;
    private RecyclerAdapterMyPlans adapter;
    private ArrayList<GetTrainingModel> mModels = new ArrayList<>();
    private boolean isSaved = false;
    private TextView countLike;
    private ImageView like;
    private int position;
    private ImageView save;
    private TextView countSaved;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_plans, container, false);

        mBinding = DataBindingUtil.bind(view);

        initRecyclerView(mModels);
        initRequest();
        initListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        initRequest();
        super.onResume();
    }

    private void initRequest() {
        HashMap<String, String> map = new HashMap<>();
        if (Connection.isNetworkAvailable(getContext())) {
            mBinding.refresh.setRefreshing(true);
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            if (isSaved)
                Request.getInstance().getSavedPlans(map, this);
            else {
                Request.getInstance().getUnpublicPlans(map, this);
            }
        } else {
            mBinding.refresh.setRefreshing(false);
        }
    }

    public ArrayList<GetTrainingModel> sort(ArrayList<GetTrainingModel> massive) {
        for (int i = 0; i < massive.size() / 2; i++) {
            GetTrainingModel tmp = massive.get(i);
            massive.set(i, massive.get(massive.size() - i - 1));
            massive.set(massive.size() - i - 1, tmp);
        }
        return massive;
    }


    private void initListeners() {

        mBinding.itemSearch.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchResult(mBinding.itemSearch.search.getText().toString());
            }
        });

        mBinding.sevedPlans.setOnClickListener(view1 -> {
            if (!isSaved) {
                adapter.setMy(false);
                isSaved = true;
                initRequest();
            }
        });

        mBinding.createdPlans.setOnClickListener(view1 -> {
            if (isSaved) {
                adapter.setMy(true);
                isSaved = false;
                initRequest();
            }
        });

        mBinding.fabAddTraining.setOnClickListener(view1 -> startActivity(new Intent(getContext(), CreatePlanActivity.class)));

        mBinding.refresh.setOnRefreshListener(this);
    }

    protected void goToPreview(String id, String userId) {
        Intent intent = new Intent(getContext(), PreviewPlanActivity.class);
        intent.putExtra("isWall", true);
        intent.putExtra("planId", id);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void initRecyclerView(ArrayList<GetTrainingModel> models) {
        PreCachingLayoutManager linearLayoutManager = new PreCachingLayoutManager(getActivity().getApplicationContext());
        mBinding.rvMyPlans.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterMyPlans(models, this);
        mBinding.rvMyPlans.setAdapter(adapter);
        mBinding.rvMyPlans.setNestedScrollingEnabled(true);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 || dy < 0 && mBinding.fabAddTraining.isShown())
                    mBinding.fabAddTraining.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mBinding.fabAddTraining.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        mBinding.rvMyPlans.setOnScrollListener(scrollListener);

    }

    private void searchResult(String text) {
        ArrayList<GetTrainingModel> result = new ArrayList<>();
        for (int i = 0; i < mModels.size(); i++) {
            if (mModels.get(i).getName() != null) {
                if (mModels.get(i).getName().contains(text)) {
                    result.add(mModels.get(i));
                }
            }
        }

        adapter.setTrainings(result);
    }

    protected void likePlan(String id, ImageView like, TextView countLike, boolean b, int position) {
        this.countLike = countLike;
        this.like = like;
        this.position = position;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        if (Connection.isNetworkAvailable(getContext())) {
            if (!b) {
                Request.getInstance().like(map, this);
            }
            if (b) {
                if (mModels.get(position).getLiked() == 1)
                    Request.getInstance().dislikePlan(map, this);
                if (mModels.get(position).getLiked() != 1)
                    Request.getInstance().like(map, this);
            }
        }
    }

    protected void savePlan(String id, ImageView save, int position, TextView countSaved) {
        this.countSaved = countSaved;
        this.save = save;
        this.position = position;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        if (Connection.isNetworkAvailable(getContext())) {
            if (mModels.get(position).getIsSaved() == 1)
                Request.getInstance().unSavePlan(map, this);
            if (mModels.get(position).getIsSaved() != 1)
                Request.getInstance().savePlan(map, this);
        }
    }

    public void publicationPlan(String id) {
        HashMap<String, String> map = new HashMap<>();
        if (Connection.isNetworkAvailable(getContext())) {
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("plan_id", id);
            map.put("status", "1");
            Request.getInstance().changePlanStatus(map, this);
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mBinding.itemSearch.search.setText("");
        initRequest();
    }

    @Override
    public void onSuccess(GetPlansModel info, String text) {
        if (isSaved) {
            mModels.clear();
            mModels.addAll(sort(info.getTrainings()));
            mBinding.refresh.setRefreshing(false);
            adapter.setTrainings(mModels);
            adapter.notifyDataSetChanged();
        } else {
            mModels.clear();
            mModels.addAll(sort(info.getTrainings()));
            adapter.setTrainings(mModels);
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("user_id", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            Request.getInstance().getUserPlans(map, this);
        }
    }

    @Override
    public void onSuccess(GetPlansModel info) {
        mModels.addAll(info.getTrainings());
        mBinding.refresh.setRefreshing(false);
        adapter.setTrainings(mModels);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    public void onSuccess(String info) {
        if (info.equals("statusChange")) {
            Toast.makeText(getContext(), "Published on", Toast.LENGTH_SHORT).show();
        }
        if (info.equals("like") || info.equals("dislike")) {
            if (info.equals("like")) {
                if (mModels.get(position).getLiked() != 1) {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_full_red));
                    countLike.setText(
                            CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getTrainings().get(position).getLikes()) + 1))
                    );
                    countLike.setTextColor(Color.parseColor("#FFFFFF"));
                    adapter.getTrainings().get(position).setLikes(CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getTrainings().get(position).getLikes()) + 1)));
                    mModels.get(position).setLiked(1);
                }
            }
            if (info.equals("dislike")) {
                if (mModels.get(position).getLiked() == 1) {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
                    countLike.setText(
                            CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getTrainings().get(position).getLikes()) - 1))
                    );
                    countLike.setTextColor(Color.parseColor("#000000"));
                    adapter.getTrainings().get(position).setLikes(CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getTrainings().get(position).getLikes()) - 1)));
                    mModels.get(position).setLiked(0);
                }
            }
            like.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animation_scale_like));
        }

        if (info.equals("save") || info.equals("unsave")) {
            if (info.equals("save")) {
                if (mModels.get(position).getIsSaved() != 1) {
                    save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_full_black));
                    countSaved.setText(CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getTrainings().get(position).getSaved()) + 1))
                    );
                    adapter.getTrainings().get(position).setSaved(String.valueOf(Integer.valueOf(adapter.getTrainings().get(position).getSaved()) + 1));
                    mModels.get(position).setIsSaved(1);
                }
            }
            if (info.equals("unsave")) {
                if (mModels.get(position).getIsSaved() == 1) {
                    save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_black));
                    countSaved.setText(CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getTrainings().get(position).getSaved()) - 1))
                    );
                    adapter.getTrainings().get(position).setSaved(String.valueOf(Integer.valueOf(adapter.getTrainings().get(position).getSaved()) - 1));
                    mModels.get(position).setIsSaved(0);
                }
            }
            save.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animation_scale_like));
        }
    }

    @Override
    public void onFailure(String message) {
        mBinding.refresh.setRefreshing(false);
        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
    }
}
