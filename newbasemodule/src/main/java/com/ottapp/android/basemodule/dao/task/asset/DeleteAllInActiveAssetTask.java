package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;

public class DeleteAllInActiveAssetTask extends AsyncTask<Void, Void, Boolean> {

    private AssetDataDao assetDao;

    public DeleteAllInActiveAssetTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            assetDao.deleteAllInActive();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
        return true;
    }
}
