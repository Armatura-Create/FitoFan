package com.example.alex.fitofan.interfaces;

/**
 * @author Alex Kucherenko(Godsmack)
 */
public interface ILoadingStatus<T> {

    void onSuccess(T info);

    void onFailure(String message);
}
