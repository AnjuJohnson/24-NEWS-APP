package com.ottapp.android.basemodule.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(indices = {@Index(value = {"id"},
        unique = true)})
public class AssetVideosDataModel implements Serializable,BaseMediaModel {
  @PrimaryKey(autoGenerate = true)
  private int id;
  private String name;
  private String description;
  private int active;
  private int languageId;
  private int genreId;
  private int assetTypeId;
  private int author;
  private int externalAuthor;
  //long createdDate;
  private String rtmpUrl;
  private String hlsUrl;
  private String startDate;
  private String endDate;
  private long duration;
  private int categoryId;
  private String thumbnailUrl;

    public String getVasUrl() {
        return vasUrl;
    }

    public void setVasUrl(String vasUrl) {
        this.vasUrl = vasUrl;
    }

    private String vasUrl;

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    private long modifiedDate;

    public int getPlayed_duration() {
        return played_duration;
    }

    public void setPlayed_duration(int played_duration) {
        this.played_duration = played_duration;
    }

    private int played_duration;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    private String videoId;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    private int favourite =-1;

    public int getId() {
        return id;
    }

    @Override
    public long getVId() {
        return getId();
    }

    @Override
    public String getVastUrl() {
        return getVasUrl();
    }

    @Override
    public String getSubTitleUrl() {
        return null;
    }

    @Override
    public String getContentUrl() {
        return getHlsUrl();
    }

    @Override
    public int getMediaType() {
        return assetTypeId;
    }

    @Override
    public String getDefaultThumbnailUrl() {
        return getThumbnailUrl();
    }

    @Override
    public String getMediaName() {
        return getName();
    }

    @Override
    public String getSubTitleLanguage() {
        return null;
    }

    public void setId(int id) {
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getExternalAuthor() {
        return externalAuthor;
    }

    public void setExternalAuthor(int externalAuthor) {
        this.externalAuthor = externalAuthor;
    }

//    public long getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(long createdDate) {
//        this.createdDate = createdDate;
//    }

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

  private int subCategoryId;
  private String category;
  private String subcategory;

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }
}
