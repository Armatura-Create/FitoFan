package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendExerciseModel {

    @SerializedName("time")
    @Expose
    private long time;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("count_repetitions")
    @Expose
    private int countRepetitions;

    @SerializedName("time_between")
    @Expose
    private long timeBetween;

    @SerializedName("image")
    @Expose
    private String imagePath;

    @SerializedName("is_new")
    @Expose
    private String isNew;

    @SerializedName("recovery_time")
    @Expose
    private long recoveryTime;

    @SerializedName("music_urls")
    @Expose
    private String musicUrls;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(long recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public String getMusicUrls() {
        return musicUrls;
    }

    public void setMusicUrls(String musicUrls) {
        this.musicUrls = musicUrls;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }
}
