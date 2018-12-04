package com.tfApp.android.newstv.adaptors;



import com.ottapp.android.basemodule.models.CategoryAssetsList;


public class YoutubeSnap {

    private int mGravity;
    private CategoryAssetsList playListModel;

    public YoutubeSnap(int gravity, CategoryAssetsList playListModel) {
        mGravity = gravity;
        this.playListModel = playListModel;
    }


    public int getGravity() {
        return mGravity;
    }

    public CategoryAssetsList getPlayListModel() {
        return playListModel;
    }

    public void setCategoryAssetListModel(CategoryAssetsList playListModel) {
        this.playListModel = playListModel;
    }
}
