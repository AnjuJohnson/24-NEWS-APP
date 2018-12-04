package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;

public class GetAssetCountTask extends AsyncTask<Integer, Void, Integer> {

    private AssetDataDao assetDao;

    public GetAssetCountTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected Integer doInBackground(Integer... ids) {
        try {
            return assetDao.getAssetsCount(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return 0;
        }
    }
}
