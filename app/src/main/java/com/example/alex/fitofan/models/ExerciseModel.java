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

public class ExerciseModel implements Serializable, Parcelable {

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

    @DatabaseField(columnName = "count_repetitions")
    @SerializedName("count_repetitions")
    @Expose
    private int countRepetitions;

    @DatabaseField(columnName = "audio")
    private String audio;

    @DatabaseField(columnName = "image")
    private String image;

    @DatabaseField(columnName = "time_between")
    @SerializedName("time_between")
    @Expose
    private long timeBetween;

    @DatabaseField(columnName = "recovery_time")
    @SerializedName("recovery_time")
    @Expose
    private long recoveryTime;

    public ExerciseModel() {
    }

    protected ExerciseModel(Parcel in) {
        time = in.readLong();
        name = in.readString();
        description = in.readString();
        countRepetitions = in.readInt();
        timeBetween = in.readLong();
        recoveryTime = in.readLong();
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
//        json.put("email", email);
        return json.toString().replaceAll("\\\\", "");
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
        dest.writeLong(recoveryTime);
    }
}
