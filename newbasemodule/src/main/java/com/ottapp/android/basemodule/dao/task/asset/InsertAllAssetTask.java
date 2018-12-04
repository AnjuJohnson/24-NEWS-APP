package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class InsertAllAssetTask extends AsyncTask<List<AssetVideosDataModel>, Void, Boolean> {

    private AssetDataDao appIntroCardDao;

    public InsertAllAssetTask(AssetDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(List<AssetVideosDataModel>... models) {
        try {
          List<Long> ids=  appIntroCardDao.insertAll(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
