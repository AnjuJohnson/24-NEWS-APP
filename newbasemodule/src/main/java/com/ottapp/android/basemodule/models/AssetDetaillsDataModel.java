package com.ottapp.android.basemodule.models;

import java.io.Serializable;

public class AssetDetaillsDataModel implements Serializable {
    private long id;
    private String name;
    private String description;
    private int categoryId;
    private int subCategoryId;
    private int languageId;
    private int genreId;
    private int assetTypeId;
    private String externalAuthor;
    private String rtmpUrl;
    private String hlsUrl;
    private String author;
    //private long startDate;
   // private long endDate;

    public int getPlayed_duration() {
        return played_duration;
    }

    public void setPlayed_duration(int played_duration) {
        this.played_duration = played_duration;
    }

    private int played_duration;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    private String thumbnailUrl;
    private String defaultThumbnailUrl;
    private int duration;
    //private long updatedDate;
    private long favouriteStatus;
    private long likedStatus;
    private long dislikedStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public int getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(int assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getExternalAuthor() {
        return externalAuthor;
    }

    public void setExternalAuthor(String externalAuthor) {
        this.externalAuthor = externalAuthor;
    }

    public String getRtmpUrl() {
        return rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String hlsUrl) {
        this.hlsUrl = hlsUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

//    public long getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(long startDate) {
//        this.startDate = startDate;
//    }
//
//    public long getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(long endDate) {
//        this.endDate = endDate;
//    }

    public String getDefaultThumbnailUrl() {
        return defaultThumbnailUrl;
    }

    public void setDefaultThumbnailUrl(String defaultThumbnailUrl) {
        this.defaultThumbnailUrl = defaultThumbnailUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

//    public long getUpdatedDate() {
//        return updatedDate;
//    }
//
//    public void setUpdatedDate(long updatedDate) {
//        this.updatedDate = updatedDate;
//    }

    public long getFavouriteStatus() {
        return favouriteStatus;
    }

    public void setFavouriteStatus(long favouriteStatus) {
        this.favouriteStatus = favouriteStatus;
    }

    public long getLikedStatus() {
        return likedStatus;
    }

    public void setLikedStatus(long likedStatus) {
        this.likedStatus = likedStatus;
    }

    public long getDislikedStatus() {
        return dislikedStatus;
    }

    public void setDislikedStatus(long dislikedStatus) {
        this.dislikedStatus = dislikedStatus;
    }

    public long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(long totalComments) {
        this.totalComments = totalComments;
    }

    public long getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(long totalLikes) {
        this.totalLikes = totalLikes;
    }

    public long getTotalDislikes() {
        return totalDislikes;
    }

    public void setTotalDislikes(long totalDislikes) {
        this.totalDislikes = totalDislikes;
    }

    public long getNoOfViews() {
        return noOfViews;
    }

    public void setNoOfViews(long noOfViews) {
        this.noOfViews = noOfViews;
    }

    private long totalComments;
    private long totalLikes;
    private long totalDislikes;
    private long noOfViews;
}
