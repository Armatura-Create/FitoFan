package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrainingModel {

    @SerializedName("time")
    @Expose
    private long time;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("exercises")
    @Expose
    private ArrayList<ExerciseModel> exercises;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ExerciseModel> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<ExerciseModel> exercises) {
        this.exercises = exercises;
    }

    public String toJSONString() throws JSONException {
        JSONObject json = new JSONObject();
//        json.put("email", email);
        return json.toString().replaceAll("\\\\", "");
    }
}
