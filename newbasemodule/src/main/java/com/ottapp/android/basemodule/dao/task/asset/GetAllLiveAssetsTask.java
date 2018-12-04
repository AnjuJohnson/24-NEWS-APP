package com.ottapp.android.basemodule.dao.task.asset;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class GetAllLiveAssetsTask extends AsyncTask<Void, Void, LiveData<List<AssetVideosDataModel>>> {

    private AssetDataDao assetsDao;

    public GetAllLiveAssetsTask(AssetDataDao appIntroCardDao) {
        this.assetsDao = appIntroCardDao;
    }

    @Override
    protected LiveData<List<AssetVideosDataModel>> doInBackground(Void... integers) {
        try {
            return assetsDao.getAllAssetsLive();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
