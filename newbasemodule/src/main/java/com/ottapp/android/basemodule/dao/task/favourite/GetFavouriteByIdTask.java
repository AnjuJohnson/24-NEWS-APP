package com.ottapp.android.basemodule.dao.task.favourite;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.UserFavouriteDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserFavouritesModel;

import java.util.List;

public class GetFavouriteByIdTask extends AsyncTask<Integer, Void, UserFavouritesModel> {

    private UserFavouriteDataDao favouriteDataDao;
    public GetFavouriteByIdTask(UserFavouriteDataDao appIntroCardDao) {
        this.favouriteDataDao = appIntroCardDao;
    }

    @Override
    protected UserFavouritesModel doInBackground(Integer... ids) {
        try {
            return favouriteDataDao.getFavouriteModel(ids[0]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
