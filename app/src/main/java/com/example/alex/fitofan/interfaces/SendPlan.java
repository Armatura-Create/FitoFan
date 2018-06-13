package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetPlanModel;

public interface SendPlan {

    void onSuccess(GetPlanModel info);

    void onFailure(String message);
}
