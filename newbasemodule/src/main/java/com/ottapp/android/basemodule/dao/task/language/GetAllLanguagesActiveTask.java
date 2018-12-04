package com.ottapp.android.basemodule.dao.task.language;

import android.os.AsyncTask;

import com.ottapp.android.basemodule.dao.AssetDataDao;
import com.ottapp.android.basemodule.dao.LanguageDataDao;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.LanguageModel;

import java.util.List;

public class GetAllLanguagesActiveTask extends AsyncTask<Void, Void, Integer> {

    private LanguageDataDao appIntroCardDao;

    public GetAllLanguagesActiveTask(LanguageDataDao appIntroCardDao) {
        this.appIntroCardDao = appIntroCardDao;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            return appIntroCardDao.getAllActiveCount();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            ;
            return 0;
        }

    }
}
