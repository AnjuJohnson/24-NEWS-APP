package com.ottapp.android.basemodule.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

@SuppressWarnings("unused")
@Entity(indices = {@Index(value = {"id"},
        unique = true)})
public class BannerModel extends BaseModel {
    private int id;
    private int assetId;
    private long mediaId;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    private int active;
    private String title;
    private String likedCount, commentCount;
    private String bannerUrl;
    private boolean likedStatus;

    public BannerModel() {
    }

    public BannerModel(long mediaId, String title, String likedCound, String commentCount, boolean likedStatus, String bannerUrl) {
        this.mediaId = mediaId;
        this.title = title;
        this.bannerUrl = bannerUrl;
        this.likedCount = likedCound;
        this.commentCount = commentCount;
        this.likedStatus = likedStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }


    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(String likedCount) {
        this.likedCount = likedCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isLikedStatus() {
        return likedStatus;
    }

    public void setLikedStatus(boolean likedStatus) {
        this.likedStatus = likedStatus;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }
}
