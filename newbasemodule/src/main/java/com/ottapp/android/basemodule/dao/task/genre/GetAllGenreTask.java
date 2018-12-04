package com.ottapp.android.basemodule.dao.task.genre;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.GenreDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class GetAllGenreTask extends AsyncTask<Void, Void, List<GenreModel>> {

    private GenreDataDao appIntroCardDao;

    public GetAllGenreTask(GenreDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected List<GenreModel> doInBackground(Void... voids) {
        try {
            return appIntroCardDao.getAll();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
