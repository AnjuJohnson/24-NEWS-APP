package com.ottapp.android.basemodule.dao.task.asset;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class GetAllLiveAssetActiveTask extends AsyncTask<Integer, Void, LiveData<List<AssetVideosDataModel>>> {

    private AssetDataDao assetsDao;

    public GetAllLiveAssetActiveTask(AssetDataDao appIntroCardDao) {
        this.assetsDao = appIntroCardDao;
    }

    @Override
    protected LiveData<List<AssetVideosDataModel>> doInBackground(Integer... integers) {
        try {
            return assetsDao.getAllLiveAssets(integers[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
