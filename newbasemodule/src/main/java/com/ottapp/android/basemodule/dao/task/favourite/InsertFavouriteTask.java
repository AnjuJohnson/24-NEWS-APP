package com.ottapp.android.basemodule.dao.task.favourite;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.UserFavouriteDataDao;
import com.ottapp.android.basemodule.models.UserFavouritesModel;

public class InsertFavouriteTask extends AsyncTask<UserFavouritesModel, Void, Boolean> {

    private UserFavouriteDataDao favouriteDataDao;

    public InsertFavouriteTask(UserFavouriteDataDao favouriteDataDao) {
        this.favouriteDataDao = favouriteDataDao;
    }

    @Override
    protected Boolean doInBackground(UserFavouritesModel... models) {
        try {
            favouriteDataDao.insert(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
