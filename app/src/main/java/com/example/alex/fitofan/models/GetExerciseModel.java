package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class GetExerciseModel {

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("count_repetitions")
    @Expose
    private String countRepetitions;

    @SerializedName("image_path")
    private String image;

    @SerializedName("time_between")
    @Expose
    private String timeBetween;

    @SerializedName("recovery_time")
    @Expose
    private String recoveryTime;

}
