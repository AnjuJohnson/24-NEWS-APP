package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class GetAllAssetActiveTask extends AsyncTask<Void, Void, List<AssetVideosDataModel>> {

    private AssetDataDao appIntroCardDao;

    public GetAllAssetActiveTask(AssetDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected List<AssetVideosDataModel> doInBackground(Void... voids) {
        try {
            return appIntroCardDao.getAll();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
