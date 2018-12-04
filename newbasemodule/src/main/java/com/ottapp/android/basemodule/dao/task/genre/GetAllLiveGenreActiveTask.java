package com.ottapp.android.basemodule.dao.task.genre;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.GenreDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.models.GenreModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class GetAllLiveGenreActiveTask extends AsyncTask<Integer, Void, LiveData<List<GenreModel>>> {

    private GenreDataDao assosiationDataDao;

    public GetAllLiveGenreActiveTask(GenreDataDao appIntroCardDao) {
        this.assosiationDataDao = appIntroCardDao;
    }

    @Override
    protected LiveData<List<GenreModel>> doInBackground(Integer... integers) {
        try {
            return assosiationDataDao.getAllLiveGenre();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return null;
        }

    }
}
