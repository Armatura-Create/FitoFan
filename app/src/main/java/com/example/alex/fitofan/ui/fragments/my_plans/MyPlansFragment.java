package com.example.alex.fitofan.ui.fragments.my_plans;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.databinding.FragmentMyPlansBinding;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.ui.activity.create_plan.CreatePlanActivity;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.utils.CustomDialog;
import com.example.alex.fitofan.utils.ItemClickSupport;
import com.example.alex.fitofan.utils.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class MyPlansFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FragmentMyPlansBinding mBinding;
    private View view;
    private RecyclerAdapterMyPlans adapter;
    private ArrayList<TrainingModel> mModels = new ArrayList<>();
    private Dao<TrainingModel, Integer> mTrainings;
    private final int IS_FROM_TRAINING = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_plans, container, false);

        mBinding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public void onStart() {
        initDB();
        initListeners();
        super.onStart();
    }

    private void initDB() {
        mModels.clear();
        try {
            mTrainings = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class).getTrainingDAO();
            assert mTrainings != null;
            mModels.addAll(mTrainings.queryForAll());
            mModels = sort(mModels);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initRecyclerView(mModels);
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
        // обработка нажатий по элементу списка
        ItemClickSupport.addTo(mBinding.rvMyPlans).setOnItemClickListener((recyclerView, position, v) -> {
            goToPreview(mModels.get(position));
        });

        ItemClickSupport.addTo(mBinding.rvMyPlans).setOnItemLongClickListener((recyclerView, position, v) -> {
            Dialog dialog = CustomDialog.dialogSimple(getContext(),
                    getResources().getString(R.string.select_action),
                    null,
                    getResources().getString(R.string.remove),
                    getResources().getString(R.string.edit));
            dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                Toast.makeText(getContext(), getResources().getString(R.string.removed), Toast.LENGTH_SHORT).show();
                deletePlan(position);
                dialog.dismiss();
            });

            dialog.findViewById(R.id.bt_negative).setOnClickListener(v1 -> {
                goToEdit(mModels.get(position));
                dialog.dismiss();
            });
            return false;
        });

        mBinding.searchMyPlans.setOnEditorActionListener((v, actionId, event) -> {
            searchResult(mModels, v.getText().toString());
            return true;
        });

        mBinding.refresh.setOnRefreshListener(this);
    }

    private void goToPreview(TrainingModel trainingModel) {
        Intent intent = new Intent(getContext(), PreviewPlanActivity.class);
        intent.putExtra("trainingModel", trainingModel.getId());
        intent.putExtra("isGoTo", IS_FROM_TRAINING);
        startActivity(intent);
    }

    private void goToEdit(TrainingModel trainingModel) {
        Intent intent = new Intent(getContext(), CreatePlanActivity.class);
        intent.putExtra("trainingModel", trainingModel.getId());
        startActivity(intent);
    }

    private void deletePlan(int position) {
        try {
            mTrainings.deleteById(mModels.get(position).getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onRefresh();
    }

    private void initRecyclerView(ArrayList<TrainingModel> models) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBinding.rvMyPlans.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapterMyPlans(models, this);
        mBinding.rvMyPlans.setAdapter(adapter);
        mBinding.rvMyPlans.setNestedScrollingEnabled(true);

    }

    //TODO не работатет пока как надо

    private void searchResult(ArrayList<TrainingModel> trainingModels, String text) {
        ArrayList<TrainingModel> result = new ArrayList<>();
        for (int i = 0; i < trainingModels.size(); i++) {
            int temp = 0;
            char[] chArrayModel = trainingModels.get(i).getName().toCharArray();
            char[] chArraySearch = text.toCharArray();
            for (char aChArrayModel : chArrayModel) {
                for (char aChArraySearch : chArraySearch) {
                    if (chArrayModel == chArraySearch) {
                        temp++;
                    } else {
                        temp--;
                    }
                }
                if (temp == chArraySearch.length) {
                    result.add(trainingModels.get(i));
                }
            }
        }

        adapter.setTrainings(result);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mModels.clear();
        try {
            mTrainings = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class).getTrainingDAO();
            assert mTrainings != null;
            mModels.addAll(mTrainings.queryForAll());
            mModels = sort(mModels);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter.setTrainings(mModels);
        adapter.notifyDataSetChanged();
        mBinding.refresh.setRefreshing(false);
    }
}
