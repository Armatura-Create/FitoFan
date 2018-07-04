package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetExercisePhotos;

public interface GetExercisePhotosLoader {

    void onSuccess(GetExercisePhotos info, String request);

    void onFailure(String message);
}
