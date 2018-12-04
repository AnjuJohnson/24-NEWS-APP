package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

public class InsertAssetTask extends AsyncTask<AssetVideosDataModel, Void, Boolean> {

    private AssetDataDao assetDao;

    public InsertAssetTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(AssetVideosDataModel... models) {
        try {
             assetDao.insert(models[0]);
             return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
