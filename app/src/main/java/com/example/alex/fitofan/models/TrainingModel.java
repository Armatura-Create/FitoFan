package com.example.alex.fitofan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class TrainingModel implements Serializable, Parcelable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "time")
    @SerializedName("time")
    @Expose
    private long time;

    @DatabaseField(columnName = "name")
    @SerializedName("name")
    @Expose
    private String name;

    @DatabaseField(columnName = "description")
    @SerializedName("description")
    @Expose
    private String description;

    @DatabaseField(columnName = "image")
    private String image;

    @ForeignCollectionField(eager = true)
    @DatabaseField(columnName = "exercise", dataType = DataType.SERIALIZABLE)
    @SerializedName("exercises")
    @Expose
    private ArrayList<ExerciseModel> exercises;

    private String userId;
    private String createdTime;

    public TrainingModel() {
    }

    protected TrainingModel(Parcel in) {
        id = in.readInt();
        time = in.readLong();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        exercises = in.createTypedArrayList(ExerciseModel.CREATOR);
    }

    public static final Creator<TrainingModel> CREATOR = new Creator<TrainingModel>() {
        @Override
        public TrainingModel createFromParcel(Parcel in) {
            return new TrainingModel(in);
        }

        @Override
        public TrainingModel[] newArray(int size) {
            return new TrainingModel[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//
//    public Uri getUriImage() {
//        return uriImage;
//    }
//
//    public void setUriImage(Uri uriImage) {
//        this.uriImage = uriImage;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String toJSONStringExercise() throws JSONException {
        JSONObject json = new JSONObject();
//        json.put("time", time);
//        json.put("name", name);
//        json.put("description", description);
//        json.put("user_id", id);
        json.put("exercises", exercises);
        return json.toString().replaceAll("\\\\", "");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeLong(time);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeTypedList(exercises);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
