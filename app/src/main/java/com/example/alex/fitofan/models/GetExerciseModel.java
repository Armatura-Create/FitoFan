package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;

public class GetExerciseModel {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("exercise_time")
    @Expose
    private String time;

    @SerializedName("exercise_id")
    @Expose
    private int id;

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

    @SerializedName("photos")
    @Expose
    private ArrayList<PhotoModel> photos;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<PhotoModel> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<PhotoModel> photos) {
        this.photos = photos;
    }
}
