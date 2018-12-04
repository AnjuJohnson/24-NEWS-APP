package com.ottapp.android.basemodule.dao.task.genre;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.GenreDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class InsertAllGenreTask extends AsyncTask<List<GenreModel>, Void, Boolean> {

    private GenreDataDao appIntroCardDao;

    public InsertAllGenreTask(GenreDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected Boolean doInBackground(List<GenreModel>... models) {
        try {
          List<Long> ids=  appIntroCardDao.insertAll(models[0]);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return false;
        }
    }
}
