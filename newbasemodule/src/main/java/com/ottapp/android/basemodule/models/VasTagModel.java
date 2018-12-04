package com.ottapp.android.basemodule.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = {@Index(value = {"id"},
        unique = true)})
public class VasTagModel implements Serializable{
    private long createdDate;
    private long updatedDate;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String tagName;
    private String vastTag;
    private int partnerId;
    private int monetizationProviderId;
    private int addTypeId;
    private int active;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    private int categoryId;

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getVastTag() {
        return vastTag;
    }

    public void setVastTag(String vastTag) {
        this.vastTag = vastTag;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getMonetizationProviderId() {
        return monetizationProviderId;
    }

    public void setMonetizationProviderId(int monetizationProviderId) {
        this.monetizationProviderId = monetizationProviderId;
    }

    public int getAddTypeId() {
        return addTypeId;
    }

    public void setAddTypeId(int addTypeId) {
        this.addTypeId = addTypeId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    private int createdBy;
    private int updatedBy;
}
