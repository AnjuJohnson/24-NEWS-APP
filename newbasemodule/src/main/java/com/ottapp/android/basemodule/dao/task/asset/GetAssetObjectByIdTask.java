package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class GetAssetObjectByIdTask extends AsyncTask<Integer, Void, AssetVideosDataModel> {

    private AssetDataDao assetDao;

    public GetAssetObjectByIdTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected AssetVideosDataModel doInBackground(Integer... ids) {
        try {
            return assetDao.getById(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
