package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusModel {
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

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("user")
    @Expose
    private User user;

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

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
