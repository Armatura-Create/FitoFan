package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetTrainingModel {
    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("plan_time")
    @Expose
    private String plan_time;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image_path")
    @Expose
    private String image;

    @SerializedName("creation_date")
    @Expose
    private String creationDate;

    @SerializedName("id")
    @Expose
    private String id;

}
