package com.example.alex.fitofan.utils;

import com.example.alex.fitofan.models.CountDataOnPreviewModel;
import com.example.alex.fitofan.models.ExerciseModelFromTraining;
import com.example.alex.fitofan.models.GetPlanModel;

import java.util.ArrayList;

public class CountData {

    public static CountDataOnPreviewModel mathData(GetPlanModel model) {
        long time = 0;
        int count = 0;
        long distance = 0;
        long weight = 0;
        CountDataOnPreviewModel result = new CountDataOnPreviewModel();
        ArrayList<ExerciseModelFromTraining> exercises = UnpackingTraining.buildExercises(model);

        if (exercises.size() > 0) {
            for (int i = 0; i < exercises.size(); i++) {
                if (!exercises.get(i).isRest()) {
                    count++;
                }
                if (exercises.get(i).getTime() % 10 == StaticValues.TIME) {
                    if (exercises.get(i).isRest())
                        time += exercises.get(i).getTime();
                    else
                        time += exercises.get(i).getTime() / 10;
                }

                if (exercises.get(i).getTime() % 10 == StaticValues.DISTANCE) {
                    distance += exercises.get(i).getTime() / 10;
                }

                if (exercises.get(i).getTime() % 10 == StaticValues.WEIGHT) {
                    weight += exercises.get(i).getTime() / 10;
                }
            }
        }

        result.setTimeLong(time);

        result.setCount(count + "");
        result.setWeight(weight + "");
        result.setTime(FormatTime.formatTime(time));
        result.setWeightParam("kg");

        if (distance > 1000) {
            distance = distance / 1000;
            result.setDistanceParam("km");
            result.setDistance(distance + "");
        } else {
            result.setDistance(distance + "");
            result.setDistanceParam("m");
        }

        return result;
    }

    public static String mathLikes(String likes){
        String result = likes;

        int likesCount = Integer.valueOf(likes);

        if(likesCount > 999){
            result = likesCount/1000 + "k";
        }

        return result;
    }
}
