package com.ottapp.android.basemodule.models;

public class MoreItemRequestServiceModel {
    int categoryId;

    public MoreItemRequestServiceModel(int categoryId, int maxLimit) {
        this.categoryId =categoryId;
        this.maxLimit = maxLimit;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    int maxLimit;
}
