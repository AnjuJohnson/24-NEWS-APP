package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;

public class GetLastUpdatedDateByIdAssetTask extends AsyncTask<Integer, Void, Long> {

    private AssetDataDao assetDao;

    public GetLastUpdatedDateByIdAssetTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected Long doInBackground(Integer... ids) {
        try {
            return assetDao.getLastUpdatedTimestampById(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return 0L;
        }
    }
}
