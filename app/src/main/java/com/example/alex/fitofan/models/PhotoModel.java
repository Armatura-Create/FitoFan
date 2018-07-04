package com.example.alex.fitofan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhotoModel implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("exercise_id")
    @Expose
    private int exerciseId;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("plan_id")
    @Expose
    private String planId;

    @SerializedName("image_path")
    @Expose
    private String imagePath;

    private boolean isDeleted;
    private boolean isEdit;

    public PhotoModel() {

    }

    protected PhotoModel(Parcel in) {
        id = in.readString();
        exerciseId = in.readInt();
        userId = in.readString();
        planId = in.readString();
        imagePath = in.readString();
    }

    public static final Creator<PhotoModel> CREATOR = new Creator<PhotoModel>() {
        @Override
        public PhotoModel createFromParcel(Parcel in) {
            return new PhotoModel(in);
        }

        @Override
        public PhotoModel[] newArray(int size) {
            return new PhotoModel[size];
        }
    };

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(exerciseId);
        dest.writeString(userId);
        dest.writeString(planId);
        dest.writeString(imagePath);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
