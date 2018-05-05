package com.example.alex.fitofan.ui.fragments.my_plans;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.databinding.FragmentMyPlansBinding;
import com.example.alex.fitofan.interfaces.ILoadingStatus;
import com.example.alex.fitofan.interfaces.ILoadingStatusMyPlans;
import com.example.alex.fitofan.interfaces.ILoadingStatusUserPlans;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.models.GetPlansModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.utils.Connection;
import com.example.alex.fitofan.utils.ItemClickSupport;
import com.example.alex.fitofan.utils.db.DatabaseHelper;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyPlansFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ILoadingStatus<String>, ILoadingStatusMyPlans {

    FragmentMyPlansBinding mBinding;
    private ProgressDialog mProgressDialog;
    private View view;
    private RecyclerAdapterMyPlans adapter;
    private ArrayList<TrainingModel> mModelsCreated = new ArrayList<>();
    private Dao<TrainingModel, Integer> mTrainings;
    private ArrayList<TrainingModel> mModelsSaves = new ArrayList<>();
    private boolean isSaved = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_plans, container, false);

        mBinding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onStart() {
        mProgressDialog = new ProgressDialog(view.getContext());
        mProgressDialog.setCancelable(false);
        initRequest();
        initListeners();
        initRecyclerView(mModelsCreated);
        super.onStart();
    }

    private void initDB() {
        mModelsCreated.clear();
        try {
            mTrainings = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class).getTrainingDAO();
            assert mTrainings != null;
            mModelsCreated.addAll(mTrainings.queryForAll());
            mModelsCreated = sort(mModelsCreated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initRecyclerView(mModelsCreated);
    }

    private void initRequest() {
        if (Connection.isNetworkAvailable(getContext())) {
            mBinding.refresh.setRefreshing(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            map.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
            Request.getInstance().getSavedPlans(map, this);
        }
    }

    public ArrayList<TrainingModel> sort(ArrayList<TrainingModel> massive) {
        for (int i = 0; i < massive.size() / 2; i++) {
            TrainingModel tmp = massive.get(i);
            massive.set(i, massive.get(massive.size() - i - 1));
            massive.set(massive.size() - i - 1, tmp);
        }
        return massive;
    }

    private void initListeners() {

        mBinding.searchMyPlans.setOnEditorActionListener((v, actionId, event) -> {
            searchResult(v.getText().toString());
            return true;
        });

        mBinding.sevedPlans.setOnClickListener(view1 -> {
            isSaved = true;
            initRequest();
        });

        mBinding.createdPlans.setOnClickListener(view1 -> {
            isSaved = false;
            initDB();
        });

        mBinding.fabAddTraining.setOnClickListener(view1 -> startActivity(new Intent(getContext(), CreatePlanActivity.class)));

        mBinding.refresh.setOnRefreshListener(this);
    }

    protected void goToPreview(int id) {
        Intent intent = new Intent(getContext(), PreviewPlanActivity.class);
        if (!isSaved)
            intent.putExtra("trainingModel", id);
        if (isSaved) {
            intent.putExtra("isWall", true);
            intent.putExtra("planId", String.valueOf(id));
        }
        startActivity(intent);
    }

    private void initRecyclerView(ArrayList<TrainingModel> models) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvMyPlans.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterMyPlans(models, mProgressDialog, this);
        mBinding.rvMyPlans.setAdapter(adapter);
        mBinding.rvMyPlans.setNestedScrollingEnabled(true);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int visibleItemCount = linearLayoutManager.getChildCount();//смотрим сколько элементов на экране
//                int totalItemCount = linearLayoutManager.getItemCount();//сколько всего элементов
//                int firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента

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
        ArrayList<TrainingModel> result = new ArrayList<>();
        for (int i = 0; i < mModelsCreated.size(); i++) {
            if (mModelsCreated.get(i).getName() != null) {
                if (mModelsCreated.get(i).getName().contains(text)) {
                    result.add(mModelsCreated.get(i));
                }
            }
        }

        adapter.setTrainings(result);
    }

    private void setTraining(GetPlansModel training) {
        if (training.getTrainings() != null) {
            mModelsSaves.clear();
            ArrayList<ExerciseModel> exerciseModels = new ArrayList<>();
            for (int i = 0; i < training.getTrainings().size(); i++) {
                TrainingModel temp = new TrainingModel();
                temp.setExercises(exerciseModels);
                temp.setTime(Long.valueOf(training.getTrainings().get(i).getPlan_time()));
                temp.setName(training.getTrainings().get(i).getName());
                temp.setDescription(training.getTrainings().get(i).getDescription());
                temp.setImage(training.getTrainings().get(i).getImage());
                temp.setId(Integer.valueOf(training.getTrainings().get(i).getId()));
                mModelsSaves.add(temp);
            }

            Log.e("setTraining:", new Gson().toJson(mModelsSaves));

            adapter.setTrainings(mModelsSaves);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        if (!isSaved) {
            mModelsCreated.clear();
            try {
                mTrainings = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class).getTrainingDAO();
                assert mTrainings != null;
                mModelsCreated.addAll(mTrainings.queryForAll());
                mModelsCreated = sort(mModelsCreated);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            initRequest();
        }
        adapter.setTrainings(mModelsCreated);
        mBinding.refresh.setRefreshing(false);
    }

    @Override
    public void onSuccess(String info) {
        mBinding.refresh.setRefreshing(false);
        mProgressDialog.cancel();
        Toast.makeText(getContext(), "Ok", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(GetPlansModel info) {
        mBinding.refresh.setRefreshing(false);
        setTraining(info);
        Log.e("onSuccess: ", new Gson().toJson(info));
    }

    @Override
    public void onFailure(String message) {
        mBinding.refresh.setRefreshing(false);
        mProgressDialog.cancel();
        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
    }
}
