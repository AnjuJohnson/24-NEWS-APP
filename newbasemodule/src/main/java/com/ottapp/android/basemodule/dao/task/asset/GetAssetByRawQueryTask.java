package com.ottapp.android.basemodule.dao.task.asset;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

public class GetAssetByRawQueryTask extends AsyncTask<SupportSQLiteQuery, Void, AssetVideosDataModel> {

    private AssetDataDao appIntroCardDao;

    public GetAssetByRawQueryTask(AssetDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected AssetVideosDataModel doInBackground(SupportSQLiteQuery... queries) {
        try {
            return appIntroCardDao.getByRawQuery(queries[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
