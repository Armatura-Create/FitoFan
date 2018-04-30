package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetWallModel {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("training_plans")
    @Expose
    private ArrayList<GetTrainingModel> training;

    @SerializedName("users")
    @Expose
    private ArrayList<User> users;

    public ArrayList<GetTrainingModel> getTraining() {
        return training;
    }

    public void setTraining(ArrayList<GetTrainingModel> training) {
        this.training = training;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

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

}
