package com.ottapp.android.basemodule.models;

public class FavouriteRequestModel {
  private int assetId;

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    private int active;
}
