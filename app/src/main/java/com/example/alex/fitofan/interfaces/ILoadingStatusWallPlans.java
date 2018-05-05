package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetWallModel;

public interface ILoadingStatusWallPlans {
    void onSuccess(GetWallModel info);

    void onFailure(String message);
}
