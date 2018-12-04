package com.ottapp.android.basemodule.dao.task.favourite;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.UserFavouriteDataDao;

public class DeleteFavouriteTask extends AsyncTask<Integer, Void, Boolean> {

    private UserFavouriteDataDao favouriteDataDao;

    public DeleteFavouriteTask(UserFavouriteDataDao favouriteDataDao) {
        this.favouriteDataDao = favouriteDataDao;
    }

    @Override
    protected Boolean doInBackground(Integer... models) {
        try {
            favouriteDataDao.deleteById(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
