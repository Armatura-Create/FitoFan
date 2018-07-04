package com.example.alex.fitofan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetPlanModel implements Serializable, Parcelable {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("get")
    @Expose
    private String get;

    @SerializedName("post")
    @Expose
    private String post;

    @SerializedName("training_plan")
    @Expose
    private GetTrainingModel training;

    @SerializedName("exercises")
    @Expose
    private ArrayList<GetExerciseModel> exercises;

    public GetPlanModel(){

    }

    protected GetPlanModel(Parcel in) {
        status = in.readInt();
        error = in.readInt();
        get = in.readString();
        post = in.readString();
        training = in.readParcelable(GetTrainingModel.class.getClassLoader());
        exercises = in.createTypedArrayList(GetExerciseModel.CREATOR);
    }

    public static final Creator<GetPlanModel> CREATOR = new Creator<GetPlanModel>() {
        @Override
        public GetPlanModel createFromParcel(Parcel in) {
            return new GetPlanModel(in);
        }

        @Override
        public GetPlanModel[] newArray(int size) {
            return new GetPlanModel[size];
        }
    };

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

    public GetTrainingModel getTraining() {
        return training;
    }

    public void setTraining(GetTrainingModel training) {
        this.training = training;
    }

    public ArrayList<GetExerciseModel> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<GetExerciseModel> exercises) {
        this.exercises = exercises;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeInt(error);
        dest.writeString(get);
        dest.writeString(post);
        dest.writeParcelable(training, flags);
        dest.writeTypedList(exercises);
    }
}
