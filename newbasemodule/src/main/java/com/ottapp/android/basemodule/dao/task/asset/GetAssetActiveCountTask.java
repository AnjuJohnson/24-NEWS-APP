package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;

public class GetAssetActiveCountTask extends AsyncTask<Void, Void, Integer> {

    private AssetDataDao assetDataDao;

    public GetAssetActiveCountTask(AssetDataDao assetDataDao) {
        this.assetDataDao = assetDataDao;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return assetDataDao.getAllActiveCount();
    }
}
