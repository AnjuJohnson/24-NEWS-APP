package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class GetAssetBySelectedCategory extends AsyncTask<Void, Void, List<AssetVideosDataModel>> {

    private AssetDataDao assetDao;
    private int limit = 10;
    private int categoryId;

    public GetAssetBySelectedCategory(AssetDataDao assetDataDao, int categoryId) {
        this.assetDao = assetDataDao;
        if (limit > 0) {
            this.limit = limit;
        }else{
            this.limit=0;
        }
        this.categoryId = categoryId;
    }

    @Override
    protected List<AssetVideosDataModel> doInBackground(Void... ids) {
        try {
            if (limit == 0)
                return assetDao.selectByCategoriesId(categoryId);
            else return assetDao.selectByCategoriesWithOutLimit(categoryId);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
