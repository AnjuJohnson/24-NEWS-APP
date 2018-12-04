package com.ottapp.android.basemodule.dao.task.asset;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.CategoryDataDao;
import com.ottapp.android.basemodule.models.VasTagModel;

public class GetVasIdTask extends AsyncTask<Integer, Void, VasTagModel> {

    private CategoryDataDao assetDao;

    public GetVasIdTask(CategoryDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected VasTagModel doInBackground(Integer... ids) {
        try {
            return assetDao.selectVasUrlId(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
