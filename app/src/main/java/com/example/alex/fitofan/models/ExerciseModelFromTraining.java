package com.example.alex.fitofan.models;

public class ExerciseModelFromTraining {

    private long time;

    private String name;

    private String description;

    private int position;

    private int type;

    private String image = null;

    private boolean isRest = false;

    private boolean isRestRest = false;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIsRest(boolean isRest) {
        this.isRest = isRest;
    }

    public boolean isRest() {
        return isRest;
    }

    public void setIsRestRest(boolean isRestRest) {
        this.isRestRest = isRestRest;
    }

    public boolean isRestRest() {
        return isRestRest;
    }
}
