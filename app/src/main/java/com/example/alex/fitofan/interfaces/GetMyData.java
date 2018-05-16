package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.User;

public interface GetMyData {

    void onSuccess(User info);

    void onFailure(String message);
}
