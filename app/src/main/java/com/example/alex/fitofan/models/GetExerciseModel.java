package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class GetExerciseModel {

    @SerializedName("exercise_time")
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

    @SerializedName("music_urls")
    @Expose
    private String musicUrls;

    @SerializedName("image_path")
    @Expose
    private String image;

    @SerializedName("time_between")
    @Expose
    private String timeBetween;

    @SerializedName("recovery_time")
    @Expose
    private String recoveryTime;

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

    public String getCountRepetitions() {
        return countRepetitions;
    }

    public void setCountRepetitions(String countRepetitions) {
        this.countRepetitions = countRepetitions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeBetween() {
        return timeBetween;
    }

    public void setTimeBetween(String timeBetween) {
        this.timeBetween = timeBetween;
    }

    public String getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(String recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public String getMusicUrls() {
        return musicUrls;
    }

    public void setMusicUrls(String musicUrls) {
        this.musicUrls = musicUrls;
    }
}
