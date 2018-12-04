package com.ottapp.android.basemodule.models;

import android.arch.persistence.room.PrimaryKey;

@SuppressWarnings("unused")
public abstract class BaseModel {
    @PrimaryKey(autoGenerate = true)
    private int localId;

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }

    private long updatedDate;

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }
}
