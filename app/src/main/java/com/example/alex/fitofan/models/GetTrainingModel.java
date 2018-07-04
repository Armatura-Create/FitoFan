package com.example.alex.fitofan.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetTrainingModel implements Serializable, Parcelable {
    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("likes")
    @Expose
    private String likes;

    @SerializedName("saved")
    @Expose
    private String saved;


    @SerializedName("inventory")
    @Expose
    private String inventory;

    @SerializedName("is_saved")
    @Expose
    private int isSaved;

    @SerializedName("parent_id")
    @Expose
    private String parentId;

    @SerializedName("is_private")
    @Expose
    private String isPrivate;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("music_urls")
    @Expose
    private String musicUrls;

    @SerializedName("comments")
    @Expose
    private String comments;

    @SerializedName("liked")
    @Expose
    private int liked;

    @SerializedName("plan_time")
    @Expose
    private String plan_time;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image_path")
    @Expose
    private String image;

    @SerializedName("creation_date")
    @Expose
    private String creationDate;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("plan_level")
    @Expose
    private String planLevel = "1.0";

    @SerializedName("user")
    @Expose
    private User user;

    private boolean isEditPhoto;

    public GetTrainingModel() {

    }

    protected GetTrainingModel(Parcel in) {
        userId = in.readString();
        likes = in.readString();
        saved = in.readString();
        inventory = in.readString();
        isSaved = in.readInt();
        parentId = in.readString();
        isPrivate = in.readString();
        status = in.readString();
        musicUrls = in.readString();
        comments = in.readString();
        liked = in.readInt();
        plan_time = in.readString();
        description = in.readString();
        name = in.readString();
        image = in.readString();
        creationDate = in.readString();
        id = in.readString();
        planLevel = in.readString();
        isEditPhoto = in.readByte() != 0;
    }

    public static final Creator<GetTrainingModel> CREATOR = new Creator<GetTrainingModel>() {
        @Override
        public GetTrainingModel createFromParcel(Parcel in) {
            return new GetTrainingModel(in);
        }

        @Override
        public GetTrainingModel[] newArray(int size) {
            return new GetTrainingModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlan_time() {
        return plan_time;
    }

    public void setPlan_time(String plan_time) {
        this.plan_time = plan_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getIsSaved() {
        return isSaved;
    }

    public void setIsSaved(int isSaved) {
        this.isSaved = isSaved;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMusicUrls() {
        return musicUrls;
    }

    public void setMusicUrls(String musicUrls) {
        this.musicUrls = musicUrls;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public boolean isEditPhoto() {
        return isEditPhoto;
    }

    public void setEditPhoto(boolean editPhoto) {
        isEditPhoto = editPhoto;
    }

    public String getPlanLevel() {
        return planLevel;
    }

    public void setPlanLevel(String planLevel) {
        this.planLevel = planLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(likes);
        dest.writeString(saved);
        dest.writeString(inventory);
        dest.writeInt(isSaved);
        dest.writeString(parentId);
        dest.writeString(isPrivate);
        dest.writeString(status);
        dest.writeString(musicUrls);
        dest.writeString(comments);
        dest.writeInt(liked);
        dest.writeString(plan_time);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(creationDate);
        dest.writeString(id);
        dest.writeString(planLevel);
        dest.writeByte((byte) (isEditPhoto ? 1 : 0));
    }
}
