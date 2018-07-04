package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetPlanModel;

public interface ILoadingPlan {

    void onSuccess(GetPlanModel info, String request);

    void onFailure(String message);
}
