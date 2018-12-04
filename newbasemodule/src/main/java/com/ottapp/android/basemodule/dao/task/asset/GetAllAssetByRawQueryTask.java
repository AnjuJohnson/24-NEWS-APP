package com.ottapp.android.basemodule.dao.task.asset;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class GetAllAssetByRawQueryTask extends AsyncTask<SupportSQLiteQuery, Void, List<AssetVideosDataModel>> {

    private AssetDataDao appIntroCardDao;

    public GetAllAssetByRawQueryTask(AssetDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected List<AssetVideosDataModel> doInBackground(SupportSQLiteQuery... queries) {
        try {
            return appIntroCardDao.getAllByRawQuery(queries[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }

    }
}
