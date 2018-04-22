package com.example.alex.fitofan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestModel {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("post")
    @Expose
    private String post;

    @SerializedName("get")
    @Expose
    private String get;

    @SerializedName("user")
    @Expose
    private List<User> user;

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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
