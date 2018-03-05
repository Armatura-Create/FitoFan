package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;

public class ExerciseModel {

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
    private int countRepetitions;

    @SerializedName("image")
    @Expose
    private MultipartBody.Part image;

    @SerializedName("time_between")
    @Expose
    private long timeBetween;

    @SerializedName("recovery_time")
    @Expose
    private long recoveryTime;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }

    public int getCountRepetitions() {
        return countRepetitions;
    }

    public void setCountRepetitions(int countRepetitions) {
        this.countRepetitions = countRepetitions;
    }

    public long getTimeBetween() {
        return timeBetween;
    }

    public void setTimeBetween(long timeBetween) {
        this.timeBetween = timeBetween;
    }

    public long getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(long recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public String toJSONString() throws JSONException {
        JSONObject json = new JSONObject();
//        json.put("email", email);
        return json.toString().replaceAll("\\\\", "");
    }
}
