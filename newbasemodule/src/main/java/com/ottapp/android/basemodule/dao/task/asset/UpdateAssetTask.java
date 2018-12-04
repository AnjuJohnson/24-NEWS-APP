package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

public class UpdateAssetTask extends AsyncTask<AssetVideosDataModel, Void, Boolean> {

    private AssetDataDao assetDataDao;

    public UpdateAssetTask(AssetDataDao assetDataDao) {
        this.assetDataDao = assetDataDao;
    }

    @Override
    protected Boolean doInBackground(AssetVideosDataModel... models) {
        try {
             assetDataDao.update(models[0]);
             return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
