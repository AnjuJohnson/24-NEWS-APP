package com.ottapp.android.basemodule.models;

import java.util.List;

public class HomeDataModel {
   private int maxLimit;

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public List<AssetVideosDataModel> getAssetList() {
        return assetList;
    }

    public void setAssetList(List<AssetVideosDataModel> assetList) {
        this.assetList = assetList;
    }

    private List<AssetVideosDataModel> assetList;
}
