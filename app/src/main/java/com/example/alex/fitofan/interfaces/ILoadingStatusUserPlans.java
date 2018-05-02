package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetWallModel;

public interface ILoadingStatusUserPlans {
    void onSuccess(GetWallModel info);

    void onFailure(String message);
}
