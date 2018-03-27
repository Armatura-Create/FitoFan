package com.example.alex.fitofan.utils;

import android.util.Log;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.ExerciseModelFromTraining;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.settings.MApplication;

import java.util.ArrayList;

/**
 * Created by alex on 28.03.18.
 */

public final class UnpackingTraining {


    public static ArrayList<ExerciseModelFromTraining> buildExercises(TrainingModel model) {

        ArrayList<ExerciseModelFromTraining> result = new ArrayList<>();
        for (int i = 0; i < model.getExercises().size(); i++) {
            for (int j = 0; j < model.getExercises().get(i).getCountRepetitions(); j++) {
                if (j < model.getExercises().get(i).getCountRepetitions() - 1) {
                    Log.e("buildExercises: ", "1");
                    ExerciseModelFromTraining temp = new ExerciseModelFromTraining();
                    temp.setName(model.getExercises().get(i).getName());
                    temp.setDescription(model.getExercises().get(i).getDescription());
                    temp.setPosition(i);
                    temp.setTime(model.getExercises().get(i).getTime());
                    result.add(temp);
                    if (model.getExercises().get(i).getTimeBetween() > 0) {
                        ExerciseModelFromTraining temp_2 = new ExerciseModelFromTraining();
                        temp_2.setTime(model.getExercises().get(i).getTimeBetween());
                        temp_2.setName(MApplication.getInstance().getMResources().getString(R.string.time_between_exercise));
                        result.add(temp_2);
                    }
                } else {
                    Log.e("buildExercises: ", "2");
                    ExerciseModelFromTraining temp = new ExerciseModelFromTraining();
                    temp.setName(model.getExercises().get(i).getName());
                    temp.setDescription(model.getExercises().get(i).getDescription());
                    temp.setPosition(i);
                    temp.setTime(model.getExercises().get(i).getTime());
                    result.add(temp);
                    if (model.getExercises().get(i).getRecoveryTime() > 0) {
                        ExerciseModelFromTraining temp_2 = new ExerciseModelFromTraining();
                        temp_2.setTime(model.getExercises().get(i).getRecoveryTime());
                        temp_2.setName(MApplication.getInstance().getMResources().getString(R.string.relax_time));
                        result.add(temp_2);
                    }
                }
            }

        }
        return result;
    }
}
