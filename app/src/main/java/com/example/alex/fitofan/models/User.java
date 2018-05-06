package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("signature")
    @Expose
    private String signature;

    @SerializedName("training_plans")
    @Expose
    private String trainingPlans;

    @SerializedName("saved_training_plans")
    @Expose
    private String savedTrainingPlans;

    @SerializedName("plan_rating")
    @Expose
    private String rating;

    @SerializedName("plan_likes")
    @Expose
    private String likes;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("image_url")
    @Expose
    private String image_url;

    @SerializedName("subscribed")
    @Expose
    private int subscribed;

    @SerializedName("subscribers")
    @Expose
    private String subscribers;

    @SerializedName("background_url")
    @Expose
    private String background_url;


    @SerializedName("location")
    @Expose
    private String location;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTrainingPlans() {
        return trainingPlans;
    }

    public void setTrainingPlans(String trainingPlans) {
        this.trainingPlans = trainingPlans;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBackground_url() {
        return background_url;
    }

    public void setBackground_url(String background_url) {
        this.background_url = background_url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getSavedTrainingPlans() {
        return savedTrainingPlans;
    }

    public void setSavedTrainingPlans(String savedTrainingPlans) {
        this.savedTrainingPlans = savedTrainingPlans;
    }

    public int getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(int subscribed) {
        this.subscribed = subscribed;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }
}
