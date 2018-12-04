package com.ottapp.android.basemodule.dao.task.genre;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.GenreDataDao;

public class DeleteAllInActiveGenreTask extends AsyncTask<Void, Void, Boolean> {

    private GenreDataDao assetDao;

    public DeleteAllInActiveGenreTask(GenreDataDao appIntroCardDao) {
        this.assetDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            assetDao.deleteAllInActive();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
        return true;
    }
}
