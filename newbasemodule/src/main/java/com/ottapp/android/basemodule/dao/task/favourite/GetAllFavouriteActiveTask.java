package com.ottapp.android.basemodule.dao.task.favourite;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.UserFavouriteDataDao;
import com.ottapp.android.basemodule.models.UserFavouritesModel;

import java.util.List;

public class GetAllFavouriteActiveTask extends AsyncTask<Void, Void, List<UserFavouritesModel>> {

    private UserFavouriteDataDao favouriteDataDao;

    public GetAllFavouriteActiveTask(UserFavouriteDataDao favouriteDataDao) {
        this.favouriteDataDao = favouriteDataDao;
    }

    @Override
    protected List<UserFavouritesModel> doInBackground(Void... voids) {
        try {
            return favouriteDataDao.getAll();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
