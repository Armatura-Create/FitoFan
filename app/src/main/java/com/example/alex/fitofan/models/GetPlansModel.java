package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetPlansModel {
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

    @SerializedName("training_plans")
    @Expose
    private  ArrayList<GetTrainingModel> trainings;

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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public ArrayList<GetTrainingModel> getTrainings() {
        return trainings;
    }

    public void setTrainings(ArrayList<GetTrainingModel> trainings) {
        this.trainings = trainings;
    }
}
