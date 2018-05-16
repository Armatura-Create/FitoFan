package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetSearchPlansModel;

public interface SearchStatus {

    void onSuccess(GetSearchPlansModel info);

    void onFailure(String message);
}
