package com.example.alex.fitofan.interfaces;

public interface ILoadingStatus<T> {

    void onSuccess(T info);

    void onFailure(String message);
}
