package com.ottapp.android.basemodule.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ottapp.android.basemodule.services.AssetMenuService;

import java.util.List;

public class AssetsViewDataModel extends AndroidViewModel {
    private LiveData<List<AssetVideosDataModel>> mAllMenusAssets;
    public AssetsViewDataModel(@NonNull Application application) {
        super(application);
        mAllMenusAssets = AssetMenuService.getServices().getAllLiveAssets();
    }

    public LiveData<List<AssetVideosDataModel>> getAllMenus(int categoryId)
    { return AssetMenuService.getServices().getAllLive(categoryId); }


    public LiveData<List<AssetVideosDataModel>> getAllAssets()
    { return mAllMenusAssets; }


}
