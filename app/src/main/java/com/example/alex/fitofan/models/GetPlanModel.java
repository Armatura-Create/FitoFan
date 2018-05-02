package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetPlanModel {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("get")
    @Expose
    private String get;

    @SerializedName("post")
    @Expose
    private String post;

    @SerializedName("training_plan")
    @Expose
    private GetTrainingModel training;

    @SerializedName("exercises")
    @Expose
    private ArrayList<GetExerciseModel> exercises;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public GetTrainingModel getTraining() {
        return training;
    }

    public void setTraining(GetTrainingModel user) {
        this.training = training;
    }

    public ArrayList<GetExerciseModel> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<GetExerciseModel> exercises) {
        this.exercises = exercises;
    }
}
