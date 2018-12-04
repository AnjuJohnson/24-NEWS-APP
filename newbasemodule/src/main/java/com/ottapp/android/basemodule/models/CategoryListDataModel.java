package com.ottapp.android.basemodule.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(indices = {@Index(value = {"id"},
        unique = true)})
public class CategoryListDataModel implements Serializable{
    private transient int positionInMenuScreen;

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    @PrimaryKey(autoGenerate = true)
    private int tableId;
    private int id;
    private String name;
    private String description;
    private long createdDate;
    private int createdBy;
    private long updatedDate;
    private String playlistUrl;
    private int partnerId;

    public List<VasTagModel> getVastTag() {
        return vastTag;
    }

    public void setVastTag(List vastTag) {
        this.vastTag = vastTag;
    }

    @Ignore
    private List<VasTagModel> vastTag = null;


//    public List<SubCategoryDataModel> getSubcategoryArray() {
//        return subcategoryArray;
//    }
//
//    @TypeConverters(SubCategoryDataModel.class)
//    private final List<SubCategoryDataModel> subcategoryArray = null;
//
    public int getPositionInMenuScreen() {
        return positionInMenuScreen;
    }

    public void setPositionInMenuScreen(int positionInMenuScreen) {
        this.positionInMenuScreen = positionInMenuScreen;
    }

    public int getId() {
        return id;
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

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getPlaylistUrl() {
        return playlistUrl;
    }

    public void setPlaylistUrl(String playlistUrl) {
        this.playlistUrl = playlistUrl;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    private int sortOrder;
    private int active;
    private int updatedBy;

}
