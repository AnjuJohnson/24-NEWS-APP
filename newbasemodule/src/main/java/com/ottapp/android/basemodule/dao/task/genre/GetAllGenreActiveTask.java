package com.ottapp.android.basemodule.dao.task.genre;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.GenreDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.models.GenreModel;

public class GetAllGenreActiveTask extends AsyncTask<Void, Void, GenreModel> {

    private GenreDataDao appIntroCardDao;

    public GetAllGenreActiveTask(GenreDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected GenreModel doInBackground(Void... voids) {
        try {
            return appIntroCardDao.getAllActiveCount();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
