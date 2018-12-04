package com.ottapp.android.basemodule.dao.task.favourite;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.UserFavouriteDataDao;
import com.ottapp.android.basemodule.models.UserFavouritesModel;

import java.util.List;

public class InsertAllFavouriteTask extends AsyncTask<List<UserFavouritesModel>, Void, Boolean> {

    private UserFavouriteDataDao favouriteDataDao;

    public InsertAllFavouriteTask(UserFavouriteDataDao favouriteDataDao) {
        this.favouriteDataDao = favouriteDataDao;
    }

    @Override
    protected Boolean doInBackground(List<UserFavouritesModel>... models) {
        try {
            favouriteDataDao.insertAll(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
