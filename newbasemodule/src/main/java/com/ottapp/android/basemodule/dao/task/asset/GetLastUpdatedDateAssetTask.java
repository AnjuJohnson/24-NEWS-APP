package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;

public class GetLastUpdatedDateAssetTask extends AsyncTask<Void, Void, Long> {

    private AssetDataDao assetDao;

    public GetLastUpdatedDateAssetTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        try {
            return assetDao.getLastUpdatedTimestamp();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return 0L;
        }
    }
}
