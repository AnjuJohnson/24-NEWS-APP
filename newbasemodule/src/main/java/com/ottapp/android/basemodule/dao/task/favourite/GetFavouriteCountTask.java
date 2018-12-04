package com.ottapp.android.basemodule.dao.task.favourite;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.UserFavouriteDataDao;

public class GetFavouriteCountTask extends AsyncTask<Void, Void, Integer> {

    private UserFavouriteDataDao favouriteDataDao;

    public GetFavouriteCountTask(UserFavouriteDataDao favouriteDataDao) {
        this.favouriteDataDao = favouriteDataDao;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return favouriteDataDao.getAllActiveCount();
    }
}
