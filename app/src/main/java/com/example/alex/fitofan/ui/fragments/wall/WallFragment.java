package com.example.alex.fitofan.ui.fragments.wall;

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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.FragmentWallBinding;
import com.example.alex.fitofan.eventbus.ReselectWallTabs;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.LikeStatus;
import com.example.alex.fitofan.interfaces.SearchStatus;
import com.example.alex.fitofan.interfaces.SubStatus;
import com.example.alex.fitofan.models.GetSearchPlansModel;
import com.example.alex.fitofan.models.GetSearchUsersModel;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.GetWallModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.CountData;
import com.example.alex.fitofan.utils.PreCachingLayoutManager;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WallFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ILoadingStatus<GetWallModel>, LikeStatus, SearchStatus, SubStatus {

    FragmentWallBinding mBinding;
    private RecyclerAdapterWall adapter;
    private int scrolling;
    private ArrayList<GetTrainingModel> models;

    private boolean isLoading;
    private boolean isRefresh;
    private ImageView like;
    private int position;
    private ImageView save;
    private TextView countLike;
    private TextView countSaved;
    private boolean isStop;
    private boolean isShowMenu;
    private Animation show_fab_1;
    private Animation show_fab_2;
    private Animation hide_fab_1;
    private Animation hide_fab_2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wall, container, false);

        mBinding = DataBindingUtil.bind(view);
        models = new ArrayList<>();
        registerForContextMenu(mBinding.itemSearch.btFilters);
        initAnimation();
        initListeners();
        initRecyclerView();
        startRequest();
        return view;
    }

    private void initAnimation() {
        show_fab_1 = AnimationUtils.loadAnimation(getContext(), R.anim.fab1_show);
        show_fab_2 = AnimationUtils.loadAnimation(getContext(), R.anim.fab2_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getContext(), R.anim.fab1_hide);
        hide_fab_2 = AnimationUtils.loadAnimation(getContext(), R.anim.fab2_hide);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        onRefresh();
        super.onStart();
    }

    @Override
    public void onResume() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReselectWallTabs(ReselectWallTabs event) {
        mBinding.rvWall.scrollToPosition(0);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        inflater.inflate(R.menu.filter_from_seach, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_test) {
            Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void startRequest() {
        if (Connection.isNetworkAvailable(getContext())) {
            scrolling = 1;
            mBinding.refresh.setRefreshing(true);
            goNexLoad();
        }
    }

    private void initListeners() {
        mBinding.refresh.setOnRefreshListener(this);

        mBinding.fabAddTraining.setOnClickListener(view1 -> {
            if (!isShowMenu) {
                showMenuFab();
            } else {
                hideMenuFab();
            }
        });

        mBinding.fabInclude.fab1.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreatePlanActivity.class));
            hideMenuFab();
        });

        mBinding.fabInclude.fab2.setOnClickListener(v -> {
            Toast.makeText(getContext(), "POST", Toast.LENGTH_SHORT).show();
            hideMenuFab();
        });

        mBinding.itemSearch.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search(mBinding.itemSearch.search.getText().toString());
            }
        });

        mBinding.itemSearch.btFilters.setOnClickListener(Objects.requireNonNull(this.getActivity())::openContextMenu);
    }

    private void showMenuFab() {
        //fab 1
        FrameLayout.LayoutParams layoutParamsFab1Show = (FrameLayout.LayoutParams) mBinding.fabInclude.fab1.getLayoutParams();
        layoutParamsFab1Show.rightMargin += (int) (mBinding.fabInclude.fab1.getWidth() * 1.7);
        layoutParamsFab1Show.bottomMargin += (int) (mBinding.fabInclude.fab1.getHeight() * 0.25);
        mBinding.fabInclude.fab1.setLayoutParams(layoutParamsFab1Show);
        mBinding.fabInclude.fab1.startAnimation(show_fab_1);
        mBinding.fabInclude.fab1.setClickable(true);

        //fab 2
        FrameLayout.LayoutParams layoutParamsFab2Show = (FrameLayout.LayoutParams) mBinding.fabInclude.fab2.getLayoutParams();
        layoutParamsFab2Show.rightMargin += (int) (mBinding.fabInclude.fab2.getWidth() * 0.25);
        layoutParamsFab2Show.bottomMargin += (int) (mBinding.fabInclude.fab2.getHeight() * 1.7);
        mBinding.fabInclude.fab2.setLayoutParams(layoutParamsFab2Show);
        mBinding.fabInclude.fab2.startAnimation(show_fab_2);
        mBinding.fabInclude.fab2.setClickable(true);
        isShowMenu = !isShowMenu;
    }

    private void hideMenuFab() {
        //fab 1
        FrameLayout.LayoutParams layoutParamsFab1Hide = (FrameLayout.LayoutParams) mBinding.fabInclude.fab1.getLayoutParams();
        layoutParamsFab1Hide.rightMargin -= (int) (mBinding.fabInclude.fab1.getWidth() * 1.7);
        layoutParamsFab1Hide.bottomMargin -= (int) (mBinding.fabInclude.fab1.getHeight() * 0.25);
        mBinding.fabInclude.fab1.setLayoutParams(layoutParamsFab1Hide);
        mBinding.fabInclude.fab1.startAnimation(hide_fab_1);
        mBinding.fabInclude.fab1.setClickable(false);

        //fab 2
        FrameLayout.LayoutParams layoutParamsFab2Hide = (FrameLayout.LayoutParams) mBinding.fabInclude.fab2.getLayoutParams();
        layoutParamsFab2Hide.rightMargin -= (int) (mBinding.fabInclude.fab2.getWidth() * 0.25);
        layoutParamsFab2Hide.bottomMargin -= (int) (mBinding.fabInclude.fab2.getHeight() * 1.7);
        mBinding.fabInclude.fab2.setLayoutParams(layoutParamsFab2Hide);
        mBinding.fabInclude.fab2.startAnimation(hide_fab_2);
        mBinding.fabInclude.fab2.setClickable(false);
        isShowMenu = !isShowMenu;
    }

    private void search(String s) {
        if (Connection.isNetworkAvailable(getContext())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("search", s);
            Request.getInstance().searchPlans(map, this);
        }
    }

    protected void likePlan(String id, ImageView like, TextView countLike, boolean isButton, int position) {
        this.countLike = countLike;
        this.like = like;
        this.position = position;
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
        map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
        map.put("plan_id", id);
        if (Connection.isNetworkAvailable(getContext())) {
            if (!isButton) {
                Request.getInstance().like(map, this);
            }
            if (isButton) {
                if (models.get(position).getLiked() == 1)
                    Request.getInstance().dislikePlan(map, this);
                if (models.get(position).getLiked() != 1)
                    Request.getInstance().like(map, this);
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
        if (Connection.isNetworkAvailable(getContext())) {
            if (models.get(position).getIsSaved() == 1)
                Request.getInstance().unSavePlan(map, this);
            if (models.get(position).getIsSaved() != 1)
                Request.getInstance().savePlan(map, this);
        }
    }

    private void initRecyclerView() {

        PreCachingLayoutManager linearLayoutManager = new PreCachingLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(), 15);
        mBinding.rvWall.setItemViewCacheSize(100);
        mBinding.rvWall.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterWall(models, this);
        mBinding.rvWall.setAdapter(adapter);
        mBinding.rvWall.setNestedScrollingEnabled(true);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();//смотрим сколько элементов на экране
                int totalItemCount = linearLayoutManager.getItemCount();//сколько всего элементов
                int firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента

                if (!isLoading) {//проверяем, грузим мы что-то или нет, эта переменная должна быть вне класса  OnScrollListener
                    if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                        isLoading = true;//ставим флаг что мы попросили еще элемены
                        if (!isStop)
                            goNexLoad();
                    }
                }

//                if (dy > 0 || dy < 0 && !isShowMenu) {
//                    mBinding.fabAddTraining.hide();
//                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mBinding.fabAddTraining.show();
                }

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (!isShowMenu) {
                        mBinding.fabAddTraining.hide();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        };
        mBinding.rvWall.setOnScrollListener(scrollListener);
    }

    private void goNexLoad() {
        if (Connection.isNetworkAvailable(getContext()) && isLoading) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("page", String.valueOf(scrolling));
            Request.getInstance().getWall(map, this);
            scrolling++;
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mBinding.rvWall.scrollToPosition(0);
        mBinding.itemSearch.search.setText("");
        isRefresh = true;
        isLoading = true;
        isStop = false;
        scrolling = 1;
        if (Connection.isNetworkAvailable(getContext())) {
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            map.put("page", "1");
            Request.getInstance().getWall(map, this);
        } else {
            mBinding.refresh.setRefreshing(false);
        }
    }

    @Override
    public void onSuccess(GetWallModel info) {
        if (isRefresh) {
            models.clear();
            models.addAll(info.getTrainings());
            adapter.setWallModels(models);
            adapter.notifyDataSetChanged();
            scrolling++;
            isLoading = false;
            isRefresh = false;
        } else if (info.getTrainings() != null && info.getTrainings().size() > 0) {
            models.addAll(info.getTrainings());
            adapter.setWallModels(models);
            adapter.notifyDataSetChanged();
            isLoading = false;
        } else {
            isStop = true;
        }
        mBinding.refresh.setRefreshing(false);
    }

    @Override
    public void onSuccessPlans(GetSearchPlansModel info) {
        if (info.getTrainings() != null && info.getTrainings().size() > 0) {
            models.clear();
            models.addAll(info.getTrainings());
            adapter.setWallModels(models);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccessUsers(GetSearchUsersModel info) {

    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    public void onSuccess(String info) {
        if (info.equals("like") || info.equals("dislike")) {
            if (info.equals("like")) {
                if (models.get(position).getLiked() != 1) {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_full_red));
                    countLike.setText(
                            CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) + 1))
                    );
                    countLike.setTextColor(Color.parseColor("#FFFFFF"));
                    adapter.getmWallModels().get(position).setLikes(
                            CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) + 1))
                    );
                    models.get(position).setLiked(1);
                }
            }
            if (info.equals("dislike")) {
                if (models.get(position).getLiked() == 1) {
                    like.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
                    countLike.setText(
                            CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) - 1))
                    );
                    countLike.setTextColor(Color.parseColor("#000000"));
                    adapter.getmWallModels().get(position).setLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getLikes()) - 1));
                    models.get(position).setLiked(0);
                }
            }
            like.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animation_scale_like));
        }

        if (info.equals("save") || info.equals("unsave")) {
            if (info.equals("save")) {
                if (models.get(position).getIsSaved() != 1) {
                    save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_full_black));
                    countSaved.setText(
                            CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getSaved()) + 1))
                    );
                    countSaved.setTextColor(Color.parseColor("#ffffff"));
                    adapter.getmWallModels().get(position).setSaved(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getSaved()) + 1));
                    models.get(position).setIsSaved(1);
                }
            }
            if (info.equals("unsave")) {
                if (models.get(position).getIsSaved() == 1) {
                    save.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_black));
                    countSaved.setText(
                            CountData.mathLikes(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getSaved()) - 1))
                    );
                    countSaved.setTextColor(Color.parseColor("#000000"));
                    adapter.getmWallModels().get(position).setSaved(String.valueOf(Integer.valueOf(adapter.getmWallModels().get(position).getSaved()) - 1));
                    models.get(position).setIsSaved(0);
                }
            }
            save.startAnimation(AnimationUtils.loadAnimation(getContext(), R.animator.animation_scale_like));
        }
    }

    @Override
    public void onFailure(String message) {
        mBinding.refresh.setRefreshing(false);
    }
}
