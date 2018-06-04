package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetSearchPlansModel;
import com.example.alex.fitofan.models.GetSearchUsersModel;

public interface SearchStatus {

    void onSuccessPlans(GetSearchPlansModel info);

    void onSuccessUsers(GetSearchUsersModel info);

    void onFailure(String message);
}
