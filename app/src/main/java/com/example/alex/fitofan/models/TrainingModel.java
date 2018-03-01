package com.example.alex.fitofan.models;

import java.util.ArrayList;

public class TrainingModel {

    private String mTotalTime;
    private String mNameTraining;
    private ArrayList<MapTrainingModel> mMapTrainingModels;

    public String getTotalTime() {
        return mTotalTime;
    }

    public void setTotalTime(String totalTime) {
        mTotalTime = totalTime;
    }

    public String getNameTraining() {
        return mNameTraining;
    }

    public void setNameTraining(String nameTraining) {
        mNameTraining = nameTraining;
    }

    public ArrayList<MapTrainingModel> getMapTrainingModels() {
        return mMapTrainingModels;
    }

    public void setMapTrainingModels(ArrayList<MapTrainingModel> mapTrainingModels) {
        mMapTrainingModels = mapTrainingModels;
    }
}
