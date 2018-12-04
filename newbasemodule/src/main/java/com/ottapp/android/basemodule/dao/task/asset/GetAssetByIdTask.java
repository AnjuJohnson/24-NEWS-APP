package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class GetAssetByIdTask extends AsyncTask<Integer, Void, List<AssetVideosDataModel>> {

    private AssetDataDao assetDao;

    public GetAssetByIdTask(AssetDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected List<AssetVideosDataModel> doInBackground(Integer... ids) {
        try {
            return assetDao.selectByCategory(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
