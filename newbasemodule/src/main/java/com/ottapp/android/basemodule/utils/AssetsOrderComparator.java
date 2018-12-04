package com.ottapp.android.basemodule.utils;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryListDataModel;

import java.util.Comparator;

public class AssetsOrderComparator implements Comparator<AssetVideosDataModel> {
    @Override
    public int compare(AssetVideosDataModel o1, AssetVideosDataModel o2) {
        return  Long.compare(o1.getModifiedDate(), o2.getModifiedDate());
    }
}