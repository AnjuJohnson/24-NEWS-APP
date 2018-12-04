package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

public class DeleteAssetTask extends AsyncTask<AssetVideosDataModel, Void, Boolean> {

    private AssetDataDao appIntroCardDao;

    public DeleteAssetTask(AssetDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(AssetVideosDataModel... models) {
        try {
             appIntroCardDao.deleteAllAssets();
             return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
