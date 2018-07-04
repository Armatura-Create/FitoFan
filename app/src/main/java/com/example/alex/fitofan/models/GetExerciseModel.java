package com.example.alex.fitofan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetExerciseModel implements Serializable, Parcelable {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("exercise_time")
    @Expose
    private String time = "0";

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("exercise_id")
    @Expose
    private int exerciseId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("count_repetitions")
    @Expose
    private String countRepetitions = "1";

    @SerializedName("music_urls")
    @Expose
    private String musicUrls;

    @SerializedName("image_path")
    @Expose
    private String image;

    @SerializedName("time_between")
    @Expose
    private String timeBetween = "0";

    @SerializedName("recovery_time")
    @Expose
    private String recoveryTime = "0";

    @SerializedName("photos")
    @Expose
    private ArrayList<PhotoModel> photos;

    @SerializedName("video_url")
    @Expose
    private String videoUrl;

    private boolean isEdit;

    private boolean isNew;

    public GetExerciseModel() {

    }

    protected GetExerciseModel(Parcel in) {
        status = in.readString();
        error = in.readString();
        time = in.readString();
        id = in.readString();
        exerciseId = in.readInt();
        name = in.readString();
        description = in.readString();
        countRepetitions = in.readString();
        musicUrls = in.readString();
        image = in.readString();
        timeBetween = in.readString();
        recoveryTime = in.readString();
        photos = in.createTypedArrayList(PhotoModel.CREATOR);
        videoUrl = in.readString();
        isEdit = in.readByte() != 0;
        isNew = in.readByte() != 0;
    }

    public static final Creator<GetExerciseModel> CREATOR = new Creator<GetExerciseModel>() {
        @Override
        public GetExerciseModel createFromParcel(Parcel in) {
            return new GetExerciseModel(in);
        }

        @Override
        public GetExerciseModel[] newArray(int size) {
            return new GetExerciseModel[size];
        }
    };

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

    public ArrayList<PhotoModel> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<PhotoModel> photos) {
        this.photos = photos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(error);
        dest.writeString(time);
        dest.writeString(id);
        dest.writeInt(exerciseId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(countRepetitions);
        dest.writeString(musicUrls);
        dest.writeString(image);
        dest.writeString(timeBetween);
        dest.writeString(recoveryTime);
        dest.writeTypedList(photos);
        dest.writeString(videoUrl);
        dest.writeByte((byte) (isEdit ? 1 : 0));
        dest.writeByte((byte) (isNew ? 1 : 0));
    }
}
