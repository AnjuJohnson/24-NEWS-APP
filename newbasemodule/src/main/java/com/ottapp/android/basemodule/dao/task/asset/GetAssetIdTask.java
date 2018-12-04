package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

public class GetAssetIdTask extends AsyncTask<Integer, Void, Integer> {

    private AssetDataDao assetDao;

    public GetAssetIdTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected Integer doInBackground(Integer... ids) {
        try {
            return assetDao.selectByAssetId(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return 0;
        }
    }
}
