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

public class ExerciseModel implements Serializable, Parcelable {

    private long time;

    private String name;

    private String description;

    private int countRepetitions = 1;

    private long timeBetween = 0;

    private String audio;

    private String image;

    private ArrayList<String> images;

    private long recoveryTime = 0;

    private boolean isEditData;

    private boolean isEditPhoto;

    public ExerciseModel(){}

    private ExerciseModel(Parcel in) {
        time = in.readLong();
        name = in.readString();
        description = in.readString();
        countRepetitions = in.readInt();
        timeBetween = in.readLong();
        audio = in.readString();
        image = in.readString();
        images = in.createStringArrayList();
        recoveryTime = in.readLong();
        isEditData = in.readByte() != 0;
        isEditPhoto = in.readByte() != 0;
    }

    public static final Creator<ExerciseModel> CREATOR = new Creator<ExerciseModel>() {
        @Override
        public ExerciseModel createFromParcel(Parcel in) {
            return new ExerciseModel(in);
        }

        @Override
        public ExerciseModel[] newArray(int size) {
            return new ExerciseModel[size];
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

    public long getRecoveryTime() {
        return recoveryTime;
    }

    public void setRecoveryTime(long recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String toJSONString() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("time", time);
        json.put("name", name);
        json.put("description", description);
        json.put("count_repetitions", countRepetitions);
        json.put("image_path", image);
        json.put("time_between", timeBetween);
        json.put("recovery_time", recoveryTime);
        return json.toString().replaceAll("\\\\", "");
    }

    public boolean isEditData() {
        return isEditData;
    }

    public void setEditData(boolean editData) {
        isEditData = editData;
    }

    public boolean isEditPhoto() {
        return isEditPhoto;
    }

    public void setEditPhoto(boolean editPhoto) {
        isEditPhoto = editPhoto;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(countRepetitions);
        dest.writeLong(timeBetween);
        dest.writeString(audio);
        dest.writeString(image);
        dest.writeStringList(images);
        dest.writeLong(recoveryTime);
        dest.writeByte((byte) (isEditData ? 1 : 0));
        dest.writeByte((byte) (isEditPhoto ? 1 : 0));
    }
}
