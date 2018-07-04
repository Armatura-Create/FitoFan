package com.example.alex.fitofan.utils;

import android.util.Log;

import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.ExerciseModelFromTraining;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.settings.MApplication;
import com.google.gson.Gson;

import java.util.ArrayList;

public final class UnpackingTraining {

    public static ArrayList<ExerciseModelFromTraining> buildExercises(GetPlanModel model) {

        Log.e("buildExercises: ", new Gson().toJson(model));

        ArrayList<ExerciseModelFromTraining> result = new ArrayList<>();
        for (int i = 0; i < model.getExercises().size(); i++) {
            for (int j = 0; j < Integer.valueOf(model.getExercises().get(i).getCountRepetitions()); j++) {
                if (j < Integer.valueOf(model.getExercises().get(i).getCountRepetitions()) - 1) {
                    ExerciseModelFromTraining temp = new ExerciseModelFromTraining();
                    temp.setName(model.getExercises().get(i).getName());
                    temp.setDescription(model.getExercises().get(i).getDescription());
                    temp.setPosition(i);
                    temp.setTime(Integer.valueOf(model.getExercises().get(i).getTime()));
                    temp.setImage(model.getExercises().get(i).getImage());
                    result.add(temp);
                    if (Long.valueOf(model.getExercises().get(i).getTimeBetween()) / 10 > 0) {
                        ExerciseModelFromTraining temp_2 = new ExerciseModelFromTraining();
                        temp_2.setTime(Long.valueOf(model.getExercises().get(i).getTimeBetween()));
                        temp_2.setImage("rest");
                        temp_2.setIsRest(true);
                        temp_2.setPosition(i);
                        temp_2.setName(MApplication.getInstance().getMResources().getString(R.string.rest));
                        result.add(temp_2);
                    }
                } else if (i < model.getExercises().size() - 1) {
                    ExerciseModelFromTraining temp = new ExerciseModelFromTraining();
                    temp.setName(model.getExercises().get(i).getName());
                    temp.setDescription(model.getExercises().get(i).getDescription());
                    temp.setPosition(i);
                    temp.setTime(Long.valueOf(model.getExercises().get(i).getTime()));
                    temp.setImage(model.getExercises().get(i).getImage());
                    result.add(temp);
                    if (Long.valueOf(model.getExercises().get(i).getRecoveryTime()) / 10 > 0) {
                        ExerciseModelFromTraining temp_2 = new ExerciseModelFromTraining();
                        temp_2.setTime(Long.valueOf(model.getExercises().get(i).getRecoveryTime()));
                        temp_2.setImage("rest");
                        temp_2.setIsRest(true);
                        temp_2.setIsRestRest(true);
                        temp_2.setPosition(i);
                        temp_2.setName(MApplication.getInstance().getMResources().getString(R.string.rest));
                        result.add(temp_2);
                    }
                } else {
                    ExerciseModelFromTraining temp = new ExerciseModelFromTraining();
                    temp.setName(model.getExercises().get(i).getName());
                    temp.setDescription(model.getExercises().get(i).getDescription());
                    temp.setPosition(i);
                    temp.setTime(Long.valueOf(model.getExercises().get(i).getTime()));
                    temp.setImage(model.getExercises().get(i).getImage());
                    result.add(temp);
                }
            }

        }
        return result;
    }
}
