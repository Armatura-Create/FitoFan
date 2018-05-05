package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetPlansModel;

public interface ILoadingStatusMyPlans {
    void onSuccess(GetPlansModel info);

    void onFailure(String message);
}
