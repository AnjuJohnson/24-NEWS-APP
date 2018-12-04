package com.ottapp.android.basemodule.models;

import java.io.Serializable;
import java.util.List;

public class CategoryAssetsList implements Serializable {


    public List<AssetVideosDataModel> getAssetVideos() {
        return assetVideos;
    }

    public void setAssetVideos(List<AssetVideosDataModel> assetVideos) {
        this.assetVideos = assetVideos;
    }

    public CategoryListDataModel getCategories() {
        return categories;
    }

    public void setCategories(CategoryListDataModel categories) {
        this.categories = categories;
    }

    private CategoryListDataModel categories;
    private List<AssetVideosDataModel> assetVideos;
}
