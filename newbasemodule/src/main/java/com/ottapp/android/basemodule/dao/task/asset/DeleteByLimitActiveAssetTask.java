package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;

public class DeleteByLimitActiveAssetTask extends AsyncTask<Void, Void, Boolean> {

    private AssetDataDao assetDao;

    public DeleteByLimitActiveAssetTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            assetDao.deleteLimitById();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
        return true;
    }
}
