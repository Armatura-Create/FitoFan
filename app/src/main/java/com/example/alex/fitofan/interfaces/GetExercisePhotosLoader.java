package com.example.alex.fitofan.interfaces;

import com.example.alex.fitofan.models.GetExercisePhotos;

public interface GetExercisePhotosLoader {

    void onSuccess(GetExercisePhotos info);

    void onFailure(String message);
}
